package de.hub.emffrag.fragmentation;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import org.junit.Assert;

import com.google.common.base.Throwables;

import de.hub.emffrag.datastore.DataIndex;
import de.hub.emffrag.datastore.DataStore;
import de.hub.emffrag.datastore.DataStoreURIHandler;
import de.hub.emffrag.datastore.KeyType;
import de.hub.emffrag.datastore.LongKeyType;
import de.hub.emffrag.fragmentation.UserObjectsCache.UserObjectsCacheListener;
import de.hub.emffrag.model.emffrag.EmfFragFactory;
import de.hub.emffrag.model.emffrag.EmfFragPackage;
import de.hub.emffrag.model.emffrag.Root;

public class FragmentedModel extends ResourceImpl {

	public static final String FRAGMENTS_INDEX_PREFIX = "f";
	public static final String ID_INDEX_PREFIX = "c";
	public static final String INDEX_CLASSES_PREFIX = "i";
	public static final String INDEX_FEATURES_PREFIX = "j";

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
	private final FragmentCache fragmentCache;
	private final DataStore dataStore;
	private final DataIndex<Long> fragmentIndex;
	private final IdIndex idIndex;
	private final Statistics statistics = new Statistics();
	private final Fragment rootFragment;

	public FragmentedModel(DataStore dataStore) {
		this(dataStore, -1);
	}

	FragmentedModel(DataStore dataStore, int cacheSize) {
		super(URI.createURI(dataStore.getURIString()));
		
		ReflectiveMetaModelRegistry.instance.registerUserMetaModel(EmfFragPackage.eINSTANCE);
		
		this.dataStore = dataStore;
		if (cacheSize == -1) {
			cacheSize = 100;
		}
		if (cacheSize < 1) {
			throw new IllegalArgumentException("A zero fragment cache is not allowed. Try a larger cache size.");
		}
		fragmentCache = new FragmentCache(cacheSize);

		this.fragmentIndex = new DataIndex<Long>(dataStore, FRAGMENTS_INDEX_PREFIX, LongKeyType.instance);
		this.idIndex = new IdIndex(dataStore);

		resourceSet = createAndConfigureAResourceSet(dataStore);

		Long first = fragmentIndex.first();
		if (first == null) {
			rootFragment = createFragment(null, null);
		} else {
			rootFragment = (Fragment) resourceSet.getResource(fragmentIndex.getURI(first), true);
			for (EObject object: rootFragment.getContents()) {
				getContents().add(((FInternalObjectImpl)object).getUserObject());
			}
		}
	}

	public class Statistics {
		private int creates = 0;
		private int loads = 0;
		private int unloads = 0;

		public int getCreates() {
			return creates;
		}

		public int getLoads() {
			return loads;
		}

		public int getUnloads() {
			return unloads;
		}
	}

	class FragmentCache {
		class CacheState implements UserObjectsCacheListener {
			private final Fragment fragment;
			private boolean cached = false;
			private int useKey;

			public CacheState(Fragment fragment) {
				super();
				this.fragment = fragment;
			}

			@Override
			public void handleUsed() {
				useKey = currentUseKey++;
				purgeUnreferencedFragments();
			}

			@Override
			public void handleReferenced() {
				if (cached) {
					remove(this);
				}
			}

			@Override
			public void handleUnReferenced() {
				if (!cached) {
					add(this);
				}
				cached = true;
				markDirty();
			}
		}

		private final int cacheSize;
		private int currentUseKey = 0;
		private boolean isDirty = false;
		private final TreeMap<CacheState, CacheState> cacheContents = new TreeMap<CacheState, CacheState>(
				new Comparator<CacheState>() {
					@Override
					public int compare(CacheState o1, CacheState o2) {
						return Integer.valueOf(o2.useKey).compareTo(o1.useKey);
					}
				});

		public FragmentCache(int cacheSize) {
			super();
			this.cacheSize = cacheSize;
		}

		synchronized void remove(CacheState state) {
			cacheContents.remove(state);
		}

		synchronized void add(CacheState state) {
			cacheContents.put(state, state);
		}

		synchronized void registerFragment(Fragment fragment) {
			fragment.getUserObjectsCache().setListener(new CacheState(fragment));
		}

		void markDirty() {
			isDirty = true;
		}

		synchronized void purgeUnreferencedFragments() {
			if (isDirty) {
				int size = cacheContents.size();
				if (size > 1.5f * cacheSize) {
					int numberOfFragmentsToRemove = Math.max(0, size - cacheSize);
					for (int i = 0; i < numberOfFragmentsToRemove; i++) {
						CacheState cacheStateToRemove = cacheContents.pollFirstEntry().getValue();
						unloadFragment(cacheStateToRemove.fragment);
					}
				}
			}
		}
	}

	private void unloadFragment(Fragment fragment) {
		if (!fragment.isLoaded()) {
			return;
		}

		statistics.unloads++;
		try {
			fragment.save(options);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		fragment.unload();
		resourceSet.getResources().remove(fragment);
	}

	/**
	 * The {@link ResourceSet}s used to store a {@link FragmentedModel} need to
	 * match specific characteristics. These are: It has to create
	 * {@link Fragment}s and {@link FInternalObjectImpl}s on loading resources;
	 * it has to use a {@link DataStoreURIHandler}; it needs to refer to the
	 * reflective version of meta models; it needs specific performance
	 * configuration loading and saving models. This methods creates a
	 * {@link ResourceSet} with the listed characteristics.
	 */
	private ResourceSet createAndConfigureAResourceSet(DataStore dataStore) {
		ResourceSet resourceSet = new ResourceSetImpl() {
			@Override
			public EObject getEObject(URI uri, boolean loadOnDemand) {
				if (uri.fragment() == null) {
					// The URI must be a ID URI
					EObject result = super.getEObject(idIndex.getObjectUriForIdUri(uri), true);
					return result;
				} else {
					return super.getEObject(uri, loadOnDemand);
				}
			}

			@Override
			protected void demandLoad(Resource resource) throws IOException {
				super.demandLoad(resource);
				statistics.loads++;
			}

		};

		resourceSet.getURIConverter().getURIHandlers().add(0, new DataStoreURIHandler(dataStore));
		resourceSet.getLoadOptions().putAll(options);
		resourceSet.getResourceFactoryRegistry().getProtocolToFactoryMap()
				.put(dataStore.getProtocol(), new XMIResourceFactoryImpl() {
					@Override
					public Resource createResource(URI uri) {
						Fragment fragment = newFragment(uri, FragmentedModel.this);
						fragmentCache.registerFragment(fragment);
						return fragment;
					}
				});
		resourceSet.setPackageRegistry(ReflectiveMetaModelRegistry.instance);

		return resourceSet;
	}
	
	/**
	 * Factory method for Fragments. Creates {@link XMIFragmentImpl} by default.
	 * Can be changed by subclasses.
	 */
	protected Fragment newFragment(URI uri, FragmentedModel model) {
		return new XMIFragmentImpl(uri, model);
	}

	/**
	 * We use a version of {@link ContentsEList} that handles inverses differently, because
	 * the resource does not need to be set on the user objects. They know that
	 * they belong to a {@link FragmentedModel} differently.
	 */
	@Override
	public EList<EObject> getContents() {
		if (contents == null) {
			contents = new ContentsEList<EObject>() {
				private static final long serialVersionUID = 1L;

				@Override
				public NotificationChain inverseAdd(EObject object, NotificationChain notifications) {										
					if (object instanceof FObjectImpl) {
						FInternalObjectImpl internalObject = ((FObjectImpl)object).fInternalObject();
						rootFragment.getContents().add(internalObject);
					} else {
						throw new IllegalArgumentException("Only FObjects can be added to fragmented models, generate you model code accordingly.");
					}
					FragmentedModel.this.attached(object);
					return notifications;
				}

				@Override
				public NotificationChain inverseRemove(EObject object, NotificationChain notifications) {
					if (FragmentedModel.this.isLoaded) {
				        FragmentedModel.this.detached(object);
				    }
					if (object instanceof FObjectImpl) {
						FInternalObjectImpl internalObject = ((FObjectImpl)object).fInternalObject();
						return internalObject.eSetResource(null, notifications);
					} else {
						throw new RuntimeException("Supposed unreachable.");
					}
				}																
			};
		}
		return contents;
	}

	public Root root() {
		if (getContents().isEmpty()) {
			Root root = EmfFragFactory.eINSTANCE.createRoot();
			getContents().add(root);
		}
		EObject eObject = getContents().get(0);
		if (eObject instanceof Root) {
			return (Root)eObject;
		} else {
			return null;
		}
	}

	ResourceSet getInternalResourceSet() {
		return resourceSet;
	}

	/**
	 * Creates a new fragment and adds it to the model. That means it creates a
	 * resource (fragment), adds it to the resource set, and creates an
	 * appropriate entry in the persistence.
	 * 
	 * @param fragmentRoot
	 *            The object that is going to be the root of the new fragment.
	 *            It can be a part of an existing fragment or something from the
	 *            realm of new objects.
	 * @param fragmentRootUserObject
	 *            The user object of the fragment root. Can be null.
	 */
	Fragment createFragment(FInternalObjectImpl fragmentRoot, FObjectImpl fragmentRootUserObject) {
		URI uri = fragmentIndex.getURI(fragmentIndex.add());
		return createFragment(uri, fragmentRoot, fragmentRootUserObject);
	}

	Fragment createFragment(URI fragmentURI, FInternalObjectImpl fragmentRoot, FObjectImpl fragmentRootUserObject) {
		Fragment newFragment = (Fragment) resourceSet.createResource(fragmentURI);

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

		statistics.creates++;
		return newFragment;
	}
	
	
	/**
	 * Fragment
	 */
	@Override
	public void load(Map<?, ?> options) throws IOException {
	
	}

	@Override
	public void delete(Map<?, ?> options) throws IOException {
		dataStore.drop();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void save(Map<?, ?> options) {
		if (options != null) {
			options.putAll((Map)FragmentedModel.options);
		} else {
			options = FragmentedModel.options;
		}
		for (Resource resource : resourceSet.getResources()) {
			try {
				resource.save(options);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}

	/**
	 * Resolved the given URI that denotes a DB entry that contains a serialized
	 * fragment.
	 * 
	 * @param uri
	 *            The containment URI to resolve.
	 * @return The resolved object.
	 */
	public EObject resolveObjectURI(URI uri) {
		return resourceSet.getEObject(uri, true);
	}

	public Statistics getStatistics() {
		return statistics;
	}

	public DataStore getDataStore() {
		return dataStore;
	}

	IdIndex getIdIndex() {
		return idIndex;
	}

	/**
	 * Only for testing purposes, hence package visibility.
	 */
	Fragment getFragment(URI fragmentURI) {
		return (Fragment) getInternalResourceSet().getResource(fragmentURI, false);
	}

	private void assertStatistic(String name, int value, int min, int max) {
		if (max != -1) {
			Assert.assertTrue("Too many " + name + " " + value, value <= max);
		}
		if (min != -1) {
			Assert.assertTrue("Too few " + name + " " + value, value >= min);
		}
	}

	void assertNumberOfLoadedFragments(int min, int max) {
		fragmentCache.purgeUnreferencedFragments();
		assertStatistic("loaded fragments", getInternalResourceSet().getResources().size(), min, max);
	}

	void assertNumberOfLoadedFragments(int size) {
		assertNumberOfLoadedFragments(size, size);
	}

	void assertStatistics(int minLoaded, int maxLoaded, int minLoads, int maxLoads, int minUnloads, int maxUnloads,
			int minCreates, int maxCreates) {
		fragmentCache.purgeUnreferencedFragments();
		assertStatistic("loaded fragments", getInternalResourceSet().getResources().size(), minLoaded, maxLoaded);
		assertStatistic("loads", statistics.getLoads(), minLoads, maxLoads);
		assertStatistic("unloads", statistics.getUnloads(), minUnloads, maxUnloads);
		assertStatistic("creates", statistics.getCreates(), minCreates, maxCreates);
	}

	private void assertIndex(DataIndex<Long> index, String name, long first, long last) {
		if (first == -1) {
			Assert.assertEquals("Wrong first key for " + name + ".", null, index.first());
		} else {
			Assert.assertEquals("Wrong first key for " + name + ".", (Long) first, index.first());
		}
		if (last == -1) {
			Assert.assertEquals("Wrong last key for " + name + ".", null, index.last());
		} else {
			Assert.assertEquals("Wrong last key for " + name + ".", (Long) last, index.last());
		}
	}

	void assertMaxFragmentsIndexSize(long max) {
		Assert.assertTrue("Fragments index too large.", fragmentIndex.last() <= max);
	}

	void assertFragmentsIndex(long first, long last) {
		assertIndex(fragmentIndex, "fragments index", first, last);
	}

	void assertIdIndex(long first, long last) {
		assertIndex(idIndex, "id index", first, last);
	}

	void assertValueSetIndex(EObject owner, EStructuralFeature feature, long min, long max) {
		new DataIndex<Long>(dataStore, FValueSetList.createPrefix(((FObjectImpl) owner).fInternalObject(), feature),
				LongKeyType.instance);
	}

	<KT> void assertIndexClassIndex(EObject owner, KT min, KT max, KeyType<KT> keyType) {

	}
}
