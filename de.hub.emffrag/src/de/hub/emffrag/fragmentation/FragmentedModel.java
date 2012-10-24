package de.hub.emffrag.fragmentation;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;

import com.google.common.base.Throwables;

import de.hub.emffrag.datastore.DataIndex;
import de.hub.emffrag.datastore.DataStore;
import de.hub.emffrag.datastore.DataStoreURIHandler;
import de.hub.emffrag.datastore.LongKeyType;

public class FragmentedModel {

	private final static XMLParserPoolImpl xmlParserPool = new XMLParserPoolImpl(true);
	private final static Map<Object, Object> options = new HashMap<Object, Object>();
	static {
		options.put(XMLResource.OPTION_EXTENDED_META_DATA, Boolean.FALSE);
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_USE_PARSER_POOL, xmlParserPool);
		HashMap<String, Boolean> parserFeatures = new HashMap<String, Boolean>();
		parserFeatures.put("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		options.put(XMLResource.OPTION_PARSER_FEATURES, parserFeatures);
	}

	private final ResourceSet resourceSet;
	private Fragment rootFragment;
	private final FragmentCache fragmentCache; // TODO handle fragment caching
												// issues

	private final DataStore persistence;
	private final DataIndex<Long> fragmentIndex;
	private final URI rootFragmentKeyURI;

	private class FragmentCache {

	}

	/**
	 * The {@link ResourceSet}s used to store a {@link FragmentedModel} need to
	 * match specific characteristics. These are: It has to create
	 * {@link FInternalObjectImpl}s on loading resources; it has to use a
	 * {@link DataStoreURIHandler}; it needs to refer to the reflective version
	 * of meta models; it needs specific performance configuration loading and
	 * saving models. This methods creates a {@link ResourceSet} with the listed
	 * characteristics.
	 */
	private ResourceSet createAndConfigureAResourceSet(DataStore dataStore, EPackage... metaModels) {
		ResourceSet resourceSet = new ResourceSetImpl();
		for (EPackage metaModel : metaModels) {
			EPackage reflectiveMetaModel = ReflectiveMetaModelRegistry.instance.registerRegularMetaModel(metaModel);
			resourceSet.getPackageRegistry().put(metaModel.getNsURI(), reflectiveMetaModel);			
		}
		resourceSet.getURIConverter().getURIHandlers().add(0, new DataStoreURIHandler(dataStore));
		resourceSet.getLoadOptions().putAll(options);

		return resourceSet;
	}

	public FragmentedModel(DataStore persistence, URI rootFragmentKeyURI, EPackage... metaModel) {
		this.persistence = persistence;
		this.fragmentIndex = new DataIndex<Long>(persistence, "f", LongKeyType.instance);

		resourceSet = createAndConfigureAResourceSet(persistence, metaModel);

		if (rootFragmentKeyURI == null) {
			rootFragment = crateFragment(null, null, null, null);
			this.rootFragmentKeyURI = rootFragment.getURI();
		} else {
			this.rootFragmentKeyURI = rootFragmentKeyURI;
			rootFragment = (Fragment) resourceSet.getResource(this.rootFragmentKeyURI, true);
		}

		fragmentCache = new FragmentCache();
	}

	public EList<EObject> getRootContents() {
		EList<EObject> contents = rootFragment.getContents();
		EList<EObject> result = new BasicEList<EObject>(contents.size());
		for (EObject internalObject : contents) {
			result.add(rootFragment.getUserObjectsCache().getUserObject((FInternalObjectImpl) internalObject));
		}
		return result;
	}

	public void addContent(EObject eObject) {
		FInternalObjectImpl internalObject = ((FObjectImpl) eObject).internalObject();
		if (internalObject.eResource() == null) {
			UserObjectsCache.newUserObjectsCache.removeCachedUserObject(internalObject);
			rootFragment.getContents().add(internalObject);
			rootFragment.getUserObjectsCache().addUserObjectToCache(internalObject, (FObjectImpl) eObject);
		} else {
			// TODO allow to move objects from one fragmented model to another
			throw new UnsupportedOperationException();
		}
	}

	protected ResourceSet getResourceSet() {
		return resourceSet;
	}

	/**
	 * Creates a new fragment. That means it creates a resource (fragment), adds
	 * it to the resource set, and creates an appropriate entry in the
	 * persistence.
	 * 
	 * @param fragmentRoot
	 *            The object that is going to be the root of the new fragment.
	 *            It can be a part of an existing fragment or something from the
	 *            realm of new objects.
	 * @param fragmentRootUserObject
	 *            The user object of the fragment root. Can be null.
	 * @param containingObject
	 *            The object that contains the root. It can be null, if this
	 *            method is called for the root fragment.
	 * @param containmentFeature
	 *            Can be null (see containingObject)
	 */
	public Fragment crateFragment(FInternalObjectImpl fragmentRoot, FObjectImpl fragmentRootUserObject,
			InternalEObject containingObject, EStructuralFeature containmentFeature) {
		Fragment newFragment = (Fragment) resourceSet.createResource(createNewFragmentURI());
		newFragment.setFragmentedModel(this);

		if (fragmentRoot != null) {
			Resource oldResource = fragmentRoot.eResource();
			Fragment oldFragment = null;
			if (oldResource != null && oldResource instanceof Fragment) {
				oldFragment = (Fragment) oldResource;
			}

			newFragment.getContents().add(fragmentRoot);
			if (fragmentRootUserObject != null) {
				newFragment.getUserObjectsCache().addUserObjectToCache(fragmentRoot, fragmentRootUserObject);
			}
			if (oldFragment != null) {
				oldFragment.getUserObjectsCache().removeCachedUserObject(fragmentRoot);
			}
		}
		// TODO handle fragment caching issues
		return newFragment;
	}

	/**
	 * Creates a new fragment in persistence.
	 * 
	 * @return The URI for a new fragment.
	 */
	private URI createNewFragmentURI() {
		return fragmentIndex.getURI(fragmentIndex.add());
	}

	/**
	 * Removes the fragment of the given root. Removes the resource and the
	 * corresponding entry in persistence.
	 * 
	 * @param fObjectImpl
	 *            The root of the fragment to delete.
	 */
	protected void removeFragment(FInternalObjectImpl fragmentRoot) {
		Fragment oldFragment = fragmentRoot.getFragment();
		try {
			oldFragment.setFragmentedModel(null);
			oldFragment.delete(null);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		// TODO handle fragment caching issues
	}

	public void save() {
		for (Resource resource : resourceSet.getResources()) {
			try {
				resource.save(options);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}

	public URI getRootFragmentURI() {
		return rootFragmentKeyURI;
	}
}
