package de.hub.emffrag.fragmentation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.BinaryResourceImpl;

import com.google.common.base.Throwables;

public class BinaryFragmentImpl extends BinaryResourceImpl implements Fragment {

	private final UserObjectsCache userObjectsCache;
	private final FragmentedModel model;

	public BinaryFragmentImpl(URI uri, FragmentedModel model) {
		super(uri);
		this.model = model;
		userObjectsCache = new UserObjectsCache();
	}

	@Override
	public UserObjectsCache getUserObjectsCache() {
		return userObjectsCache;
	}

	@Override
	public FragmentedModel getFragmentedModel() {
		return model;
	}
	
	@Override
	public void detached(EObject eObject) {
		super.detached(eObject);
		if (getContents().isEmpty()) {
			try {
				delete(null);
			} catch (IOException e) {
				Throwables.propagate(e);
			}
		}
	}

	/**
	 * EMF does not break down the reference graph of a resources contents
	 * property. Under some circumstances the JVM cannot remove this contents
	 * even if the resource is unloaded and removed from the resource set. This
	 * change in the EMF standard behavior of the resources fixes that.
	 */
	@Override
	protected void doUnload() {
	    Iterator<EObject> allContents = getAllProperContents(unloadingContents); 
	    
		super.doUnload();
		while (allContents.hasNext())
	    {
			FInternalObjectImpl internalObject = (FInternalObjectImpl) allContents.next();
			internalObject.trulyUnload();
	    }
	}
	
	@Override
	protected void doSave(OutputStream outputStream, Map<?, ?> options) throws IOException {
		// This is a copy of the super method, except for the market pieces
		// of code
		if (outputStream instanceof URIConverter.Saveable) {
			((URIConverter.Saveable) outputStream).saveResource(this);
		} else {
			boolean buffer = !(outputStream instanceof BufferedOutputStream);
			if (buffer) {
				int bufferCapacity = getBufferCapacity(options);
				if (bufferCapacity > 0) {
					outputStream = new BufferedOutputStream(outputStream, bufferCapacity);
				} else {
					buffer = false;
				}
			}

			try {
				// We use our own EObjectOutputStream
				// EObjectOutputStream eObjectOutputStream = new EObjectOutputStream(outputStream, options);
				EObjectOutputStream eObjectOutputStream = new MyEObjectOutputStream(outputStream, options);
				eObjectOutputStream.saveResource(this);
			} finally {
				if (buffer) {
					outputStream.flush();
				}
			}
		}
	}
	
	

	@Override
	protected void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		// This is a copy of the super method, except for the market pieces
		// of code
		if (inputStream instanceof URIConverter.Loadable) {
			((URIConverter.Loadable) inputStream).loadResource(this);
		} else {
			if (!(inputStream instanceof BufferedInputStream)) {
				int bufferCapacity = getBufferCapacity(options);
				if (bufferCapacity > 0) {
					inputStream = new BufferedInputStream(inputStream, bufferCapacity);
				}
			}

			// We use our own EObjectOutputStream
			// EObjectInputStream eObjectInputStream = new EObjectInputStream(inputStream, options);
			EObjectInputStream eObjectInputStream = new MyEObjectInputStream(inputStream, options);
			eObjectInputStream.loadResource(this);
		}
	}

	private class MyEObjectInputStream extends EObjectInputStream {
		
		public MyEObjectInputStream(InputStream is, Map<?, ?> options) throws IOException {
			super(is, options);
		}	
		
		@Override
		 protected EPackageData readEPackage() throws IOException { 
		 	EPackageData ePackageData = super.readEPackage();
			if (resourceSet != null) {
				EPackage ePackage = resourceSet.getPackageRegistry().getEPackage(ePackageData.ePackage.getNsURI());
				if (ePackage != null) {
					ePackageData.ePackage = ePackage;
				}
				if (ePackageData.eClassData.length != ePackage.getEClassifiers().size()) {
					ePackageData.eClassData = new EClassData [ePackage.getEClassifiers().size()];
				}
			}
			return ePackageData;
		}
		    
	}

	public class MyEObjectOutputStream extends EObjectOutputStream {
		boolean isWritingCrossReferenceURI = false;
		FInternalObjectImpl currentObject = null;

		public MyEObjectOutputStream(OutputStream os, Map<?, ?> options) throws IOException {
			super(os, options);
		}

		@Override
		public void saveEObject(InternalEObject internalEObject, Check check) throws IOException {
			// Ensure that URIs in the id
			// index are saved, when the object is saved.					
			FInternalObjectImpl fInternalObject = (FInternalObjectImpl)internalEObject;			
			fInternalObject.getId(false);
				
			if (check == Check.RESOURCE) {
				isWritingCrossReferenceURI = true;
				currentObject = (FInternalObjectImpl)internalEObject;
			} else {
				isWritingCrossReferenceURI = false;
			}
			super.saveEObject(internalEObject, check);
		}

		@Override
		public void writeURI(URI uri, String uriFragment) throws IOException {
			if (!isWritingCrossReferenceURI) {
				super.writeURI(uri, uriFragment);
			} else {			
				if (currentObject.hasId()) {
					Fragment fragment = (Fragment)currentObject.eResource();
					String id = currentObject.getId(false);
					if (FInternalObjectImpl.isPreliminary(id)) {
						throw new NotInAFragmentedModelException();
					}
					uri = fragment.getFragmentedModel().getIdIndex().createIdUri(id);
					super.writeURI(uri, null);					
				} else {
					super.writeURI(uri, uriFragment);
				}
			}
		}
	}
}
