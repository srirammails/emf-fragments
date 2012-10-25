package de.hub.emffrag.test;

import java.util.Random;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.junit.Assert;
import org.junit.Test;

import de.hub.emffrag.datastore.LongKeyType;
import de.hub.emffrag.fragmentation.FObjectImpl;
import de.hub.emffrag.fragmentation.FragmentedModel;
import de.hub.emffrag.testmodels.frag.testmodel.TestModelFactory;
import de.hub.emffrag.testmodels.frag.testmodel.TestModelPackage;
import de.hub.emffrag.testmodels.frag.testmodel.TestObject;

public class BasicFragmentationTests extends AbstractFragmentationTests {
	
	
	/**
	 * Test the pure creation of an empty fragmented model.
	 */
	@Test
	public void testEmpty() {
		FragmentedModel model = new FragmentedModel(createTestDataStore(), null, TestModelPackage.eINSTANCE);
		model.save();
	}

	/**
	 * Test adding a single object to an empty fragmented model as new root
	 * object.
	 */
	@Test
	public void testAddRootObject() {		
		model.addContent(object1);
		assertRootFragment(object1);
		model.save();
		reinitializeModel();
		assertHasModelRootFragment();
	}

	/**
	 * Test adding a object to a fragmenting reference.
	 */
	@Test
	public void testAddFragment() {
		model.addContent(object1);
		object1.getFragmentedContents().add(object2);
		model.save();

		reinitializeModel();
		object1 = assertHasModelRootFragment();
		object2 = assertHasContents(object1, metaModel.getTestObject_FragmentedContents());
		assertContainment(object1, object2, metaModel.getTestObject_FragmentedContents(), true);
		assertIndexDimenions(dataStore, "f", 0l, 1l, LongKeyType.instance);
	}

	@Test
	public void testRemoveObject() {
		model.addContent(object1);
		assertRootFragment(object1);
		object1.getRegularContents().add(object2);
		model.save();		
				
		reinitializeModel();
		assertIndexDimenions(dataStore, "f", 0l, 0l, LongKeyType.instance);
		object1 = assertHasModelRootFragment();
		object2 = assertHasContents(object1, metaModel.getTestObject_RegularContents());
		assertContainment(object1, object2, metaModel.getTestObject_RegularContents(), false);		

		Assert.assertTrue(object1.getRegularContents().remove(object2));
		model.save();

		reinitializeModel();
		object1 = assertHasModelRootFragment();
		Assert.assertTrue(object1.eContents().isEmpty());	
		assertIndexDimenions(dataStore, "f", 0l, 0l, LongKeyType.instance);
	}

	@Test
	public void testRemoveRootObject() {

	}

	@Test
	public void testRemoveFragmentRoot() {
		model.addContent(object1);
		assertRootFragment(object1);
		object1.getFragmentedContents().add(object2);
		model.save();
		
		assertIndexDimenions(dataStore, "f", 0l, 1l, LongKeyType.instance);
		
		object1.getFragmentedContents().remove(object2);
		model.save();

		reinitializeModel();
		object1 = assertHasModelRootFragment();
		Assert.assertTrue(object1.eContents().isEmpty());	
		assertIndexDimenions(dataStore, "f", 0l, 0l, LongKeyType.instance);		
	}

	private TestObject addObject(TestObject container, boolean fragmented) {
		TestObject contents = TestModelFactory.eINSTANCE.createTestObject();
		contents.setName("testValue");

		if (container != null) {
			if (fragmented) {
				container.getFragmentedContents().add(contents);
			} else {
				container.getRegularContents().add(contents);
			}
		}

		return contents;
	}
	
	@SuppressWarnings("unchecked")
	private boolean removeObject(TestObject contents) {
		EStructuralFeature containingFeature = contents.eContainingFeature();
		((EList<EObject>) contents.eContainer().eGet(containingFeature)).remove(contents);
		return containingFeature.getName().equals(TestModelPackage.eINSTANCE.getTestObject_FragmentedContents().getName());				
	}

	/**
	 * This test arbitrarily adds and removes objects forming a chain of
	 * containers containing each other. This test does not test the reuse of a
	 * once remove object.
	 */
	@Test
	public void testContiniousAddAndRemove() {
		TestObject container = addObject(null, false);
		model.addContent(container);
		
		Random random = new Random(0);
		int fragmentationDepth = 0;
		
		try {
			for (int i = 0; i < 100; i++) {
				if (container.eContainer() == null || random.nextBoolean()) {
					boolean fragmenting = random.nextBoolean();
					container = addObject(container, fragmenting);
					if (fragmenting) {
						fragmentationDepth++;
					}
				} else {
					TestObject newContainer = (TestObject)container.eContainer();
					if (removeObject(container)) {
						fragmentationDepth--;
					}				
					container = newContainer;
				}
				model.save();
				assertIndexDimenions(dataStore, "f", 0l, (long)fragmentationDepth, LongKeyType.instance);
			}
			while (container.eContainer() != null) {
				TestObject newContainer = (TestObject)container.eContainer();
				removeObject(container);
				container = newContainer;
			}
		} catch (RuntimeException e) {
			System.out.println(dataStore);
			throw e;
		}

		Assert.assertNotNull(container.eResource());
		model.save();

		reinitializeModel();
		reinitializeModel();
		object1 = assertHasModelRootFragment();
		Assert.assertTrue(object1.eContents().isEmpty());	
		assertIndexDimenions(dataStore, "f", 0l, 0l, LongKeyType.instance);	
	}
	
	@SuppressWarnings("unused")
	private void print(TestObject object2) {
		System.out.println(System.identityHashCode(object2));
		System.out.println(System.identityHashCode(((FObjectImpl)object2).internalObject()));
		System.out.println(((FObjectImpl)object2).internalObject().eResource().getURI());
		System.out.println(object2.eResource().getURI());
		System.out.println(object2.eContainer().eResource().getURI());
		System.out.println(object2.eContainingFeature().getName());
	}

	@Test
	public void testMoveFragmentRootToNonFragmentingReference() {
		model.addContent(object1);
		
		object1.getFragmentedContents().add(object2);						
		object1.getRegularContents().add(object2);		
		object2 = object1.getRegularContents().get(0);
		
		model.save();
		reinitializeModel();
		object1 = assertHasModelRootFragment();
		object2 = assertHasContents(object1, metaModel.getTestObject_RegularContents());
		assertContainment(object1, object2, metaModel.getTestObject_RegularContents(), false);
		assertIndexDimenions(dataStore, "f", 0l, 0l, LongKeyType.instance);
	}

	@Test
	public void testMoveFragmentRootToOtherFragmentingReference() {
		model.addContent(object1);
		object1.getFragmentedContents().add(object2);
		object2.getFragmentedContents().add(object3);		
		object1.getFragmentedContents().add(object3);
		model.save();
		
		reinitializeModel();
		object1 = assertHasModelRootFragment();
		Assert.assertEquals(2, object1.getFragmentedContents().size());
		Assert.assertTrue(object1.getFragmentedContents().get(0).getFragmentedContents().isEmpty());
		Assert.assertTrue(object1.getFragmentedContents().get(1).getFragmentedContents().isEmpty());
		assertIndexDimenions(dataStore, "f", 0l, 2l, LongKeyType.instance);
	}

	@Test
	public void testMoveObjectToFragmentingReference() {
		model.addContent(object1);
		object1.getRegularContents().add(object2);
		object1.getFragmentedContents().add(object2);
		model.save();
		
		reinitializeModel();
		object1 = assertHasModelRootFragment();
		object2 = assertHasContents(object1, metaModel.getTestObject_FragmentedContents());
		assertContainment(object1, object2, metaModel.getTestObject_FragmentedContents(), true);
		assertIndexDimenions(dataStore, "f", 0l, 1l, LongKeyType.instance);
	}

	@Test
	public void testMoveContainedObjectToAnotherFragment() {
		model.addContent(object1);
		object1.getFragmentedContents().add(object2);
		object1.getRegularContents().add(object3);
		object2.getRegularContents().add(object3);
		model.save();
		
		reinitializeModel();
		object1 = assertHasModelRootFragment();
		object2 = assertHasContents(object1, metaModel.getTestObject_FragmentedContents());
		object3 = assertHasContents(object2, metaModel.getTestObject_RegularContents());
		assertContainment(object1, object2, metaModel.getTestObject_FragmentedContents(), true);
		assertContainment(object2, object3, metaModel.getTestObject_RegularContents(), false);
		assertIndexDimenions(dataStore, "f", 0l, 1l, LongKeyType.instance);
	}
}
