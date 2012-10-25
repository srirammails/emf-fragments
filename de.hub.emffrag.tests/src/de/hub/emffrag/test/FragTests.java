/*******************************************************************************
 * Copyright 2012 Markus Scheidgen
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.hub.emffrag.test;


public class FragTests extends AbstractTests {

//	public void clearDB(String testTable) {
//		HBaseKeyValueStore hBaseKeyValueStore = new HBaseKeyValueStore();
//		if (hBaseKeyValueStore.tableExists(testTable)) {
//			hBaseKeyValueStore.deleteTable(testTable);
//		}
//	}
//
//	private class MyModule implements Module {
//		@Override
//		public void configure(Binder binder) {
//			binder.bind(ILogger.class).toInstance(new ILogger() {
//				@Override
//				public void log(int level, String message, Throwable exception) {
//					System.out.println("[" + level + "] " + message);
//				}
//			});
//			binder.bind(Integer.class).annotatedWith(Names.named(FragmentedModel.OPTION_WEAK_UNLOAD_CACHE_SIZE)).toInstance(0);
//		}
//	}
//
//	@Before
//	public void registerPackages() {
//		if (!EPackage.Registry.INSTANCE.containsKey(TestModelPackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(TestModelPackage.eINSTANCE.getNsURI(), TestModelPackage.eINSTANCE);
//		}
//		if (!EPackage.Registry.INSTANCE.containsKey(EcorePackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);
//		}
//		if (!EPackage.Registry.INSTANCE.containsKey(XMLTypePackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(XMLTypePackage.eINSTANCE.getNsURI(), XMLTypePackage.eINSTANCE);
//		}
//		if (!EPackage.Registry.INSTANCE.containsKey(CorePackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(CorePackage.eINSTANCE.getNsURI(), CorePackage.eINSTANCE);
//		}
//		if (!EPackage.Registry.INSTANCE.containsKey(DOMPackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(DOMPackage.eINSTANCE.getNsURI(), DOMPackage.eINSTANCE);
//		}
//		if (!EPackage.Registry.INSTANCE.containsKey(PrimitiveTypesPackage.eINSTANCE.getNsURI())) {
//			EPackage.Registry.INSTANCE.put(PrimitiveTypesPackage.eINSTANCE.getNsURI(), PrimitiveTypesPackage.eINSTANCE);
//		}
//	}
//
//	/**
//	 * This test loads the smallest grabats model.
//	 */
//	@Test
//	public void testResourceLoadReflective() throws Exception {
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(DOMPackage.eINSTANCE);
//		packages.add(CorePackage.eINSTANCE);
//		packages.add(PrimitiveTypesPackage.eINSTANCE);
//		packages = EcoreUtil.copyAll(packages);
//
//		ResourceSet rs = new ResourceSetImpl();
//		for (EPackage ePackage : packages) {
//			rs.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
//		}
//		Resource resource = rs.createResource(URI.createURI("../de.hub.emffrag.testmodels/models/set0.xmi"));
//		resource.load(null);
//
//		EList<EObject> contents = resource.getContents();
//		Assert.assertEquals(1, contents.size());
//		// TODO add additional assertions
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Test
//	public void testContainmentProxysReflective() throws Exception {
//		ResourceSet rs = new ResourceSetImpl();
//		EPackage packageCopy = EcoreUtil.copy(TestModelPackage.eINSTANCE);
//		rs.getPackageRegistry().put(TestModelPackage.eINSTANCE.getNsURI(), packageCopy);
//
//		Resource rootResource = rs.createResource(URI.createURI("test0.xmi"));
//		Resource contentResource = rs.createResource(URI.createURI("test1.xmi"));
//
//		EFactory factory = packageCopy.getEFactoryInstance();
//		EClass containerClass = (EClass) packageCopy.getEClassifier("Container");
//		EClass contentsClass = (EClass) packageCopy.getEClassifier("Contents");
//		EObject container = factory.create(containerClass);
//		rootResource.getContents().add(container);
//
//		EObject content = factory.create(contentsClass);
//		rootResource.getContents().add(content);
//		EStructuralFeature contentsFeature = containerClass.getEStructuralFeature("contents");
//		EList contents = (EList) container.eGet(contentsFeature);
//		contents.add(content);
//		contentResource.getContents().add(content);
//
//		Assert.assertEquals(rootResource, container.eResource());
//		Assert.assertEquals(1, rootResource.getContents().size());
//		Assert.assertEquals(1, contentResource.getContents().size());
//		Assert.assertEquals(1, ((List) container.eGet(contentsFeature)).size());
//		Assert.assertEquals(contentResource, content.eResource());
//	}
//
//	@Test
//	public void testUnloadOnFragmentedStore() throws Exception {
//		// initialize the FStore for testing
//		String testTable = "testtable";
//		Injector injector = Guice.createInjector(new MyModule() {
//			@Override
//			public void configure(Binder binder) {
//				super.configure(binder);
//				binder.bind(IKeyValueStore.class).to(TestKeyValueStore.class);
//			}
//		});
//		FStoreImpl.INSTANCE = injector.getInstance(FStoreImpl.class);
//
//		// initialize the FStore as a user would
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(TestModelPackage.eINSTANCE);
//		FStoreImpl.INSTANCE.initialize(packages, testTable, true);
//
//		Container container = ContainerBuilder.newContainerBuilder()
//				.fragmentedContents(ContentsBuilder.newContentsBuilder().value("testValue")).build();
//		FStoreImpl.INSTANCE.addContent(container);
//
//		EList<Contents> fragmentedContents = container.getFragmentedContents();
//		container = null;
//		System.gc();
//		FStoreImpl.INSTANCE.save();
//		System.out.println("---------");
//		FStoreImpl.INSTANCE.getFragmentSet().unloadUnreferencedFragments(null, false);
//
//		TestKeyValueStore kvs = (TestKeyValueStore) injector.getInstance(IKeyValueStore.class);
//		kvs.print(System.out);
//		System.out.println(fragmentedContents);
//	}
//
//	@Test
//	public void testFragmentedStoreTestObject() throws Exception {
//		String testTable = "testtable";
//		// initialize the FStore for testing
//		Injector injector = Guice.createInjector(new MyModule() {
//			@Override
//			public void configure(Binder binder) {
//				super.configure(binder);
//				binder.bind(IKeyValueStore.class).to(TestKeyValueStore.class);
//			}
//		});
//		FStoreImpl.INSTANCE = injector.getInstance(FStoreImpl.class);
//
//		// initialize the FStore as a user would
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(TestModelPackage.eINSTANCE);
//		FStoreImpl.INSTANCE.initialize(packages, testTable, true);
//
//		TestObject testObject = TestModelFactory.eINSTANCE.createTestObject();
//		FStoreImpl.INSTANCE.addContent(testObject);
//
//		for (int i = 0; i < 10000; i++) {
//			TestObject fragContent = TestModelFactory.eINSTANCE.createTestObject();
//			testObject.getFragmentedContents().add(fragContent);
//		}
//
//		TestObject regContent = TestModelFactory.eINSTANCE.createTestObject();
//		testObject.getRegularContents().add(regContent);
//
//		testObject = null;
//		regContent = null;
//		System.gc();
//		FStoreImpl.INSTANCE.getFragmentSet().unloadUnreferencedFragments(null, false);
//
//		FStoreImpl.INSTANCE.save();
//
//		String xmi = injector.getInstance(TestKeyValueStore.class).getTable("testtable", false).get("0");
//		Assert.assertTrue(!xmi.contains("#//@fragmentedContents"));
//	}
//
//	@Test
//	public void testFragmentedStore() throws Exception {		
//		TestKeyValueStore keyValueStore = new TestKeyValueStore();
//		FragmentedModel model = new FragmentedModel(keyValueStore, "testtable", TestModelPackage.eINSTANCE);
//		Container container = ContainerBuilder.newContainerBuilder().contents(ContentsBuilder.newContentsBuilder().value("testValue")).build();
//		
//		model.addContent(container);
//		model.save();
//		EList<EObject> contents = model.getContents();
//		
//		Assert.assertEquals(1, contents.size());
//		Assert.assertEquals(1, container.getContents().size());
//		Contents content = container.getContents().get(0);
//		Assert.assertEquals("testValue", content.getValue());
//		Assert.assertEquals(container, content.eContainer());
//		Table table = keyValueStore.getTable("testtable", false);
//		Assert.assertNotNull(table);
//		Assert.assertNotNull(table.get("0"));
//		Assert.assertEquals("0", table.getLargestKey());
//
//		container.getFragmentedContents().add(content);
//		
//		model.save();
//		contents = model.getContents();
//		
//		Assert.assertEquals(1, contents.size());
//		Assert.assertEquals(0, container.getContents().size());
//		Assert.assertEquals(1, container.getFragmentedContents().size());
//		content = container.getFragmentedContents().get(0);
//		Assert.assertEquals("testValue", content.getValue());
//		Assert.assertEquals(container, content.eContainer());
//		table = keyValueStore.getTable("testtable", false);
//		Assert.assertNotNull(table);
//		Assert.assertNotNull(table.get("0"));
//		Assert.assertNotNull(table.get("1"));
//		Assert.assertEquals("1", table.getLargestKey());
//
//		container.getFragmentedContents().clear();
//		
//		model.save();
//		contents = model.getContents();
//		
//		Assert.assertEquals(1, contents.size());
//		Assert.assertEquals(0, container.getContents().size());
//		Assert.assertEquals(0, container.getFragmentedContents().size());
//		Assert.assertEquals("testValue", content.getValue());
//		Assert.assertEquals(null, content.eContainer());
//		table = keyValueStore.getTable("testtable", false);
//		Assert.assertNotNull(table);
//		Assert.assertNotNull(table.get("0"));
//		Assert.assertEquals("0", table.getLargestKey());
//	}
//
//	@Test
//	public void testInterFragmentCrossReferences() throws Exception {
//		String testTable = "testtable";
//		// initialize the FStore for testing
//		Injector injector = Guice.createInjector(new MyModule() {
//			@Override
//			public void configure(Binder binder) {
//				super.configure(binder);
//				binder.bind(IKeyValueStore.class).to(TestKeyValueStore.class);
//			}
//		});
//		FStoreImpl.INSTANCE = injector.getInstance(FStoreImpl.class);
//
//		// initialize the FStore as a user would
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(TestModelPackage.eINSTANCE);
//		FStoreImpl.INSTANCE.initialize(packages, testTable, false);
//
//		TestObject to = TestModelFactory.eINSTANCE.createTestObject();
//		to.setName("root");
//		FStoreImpl.INSTANCE.addContent(to);
//		TestObject referer = TestModelFactory.eINSTANCE.createTestObject();
//		referer.setName("referer");
//		to.getFragmentedContents().add(referer);
//		TestObject referee = TestModelFactory.eINSTANCE.createTestObject();		
//		referee.setName("referee");
//		to.getRegularContents().add(referee);
//		referer.getCrossReferences().add(referee);
//
//		FStoreImpl.INSTANCE.save();
//		injector.getInstance(TestKeyValueStore.class).print(System.out);
//
//		referer = null;
//		System.gc();
//		Thread.sleep(100);
//		FStoreImpl.INSTANCE.getFragmentSet().unloadUnreferencedFragments(null, true);
//
//		TestObject inner = TestModelFactory.eINSTANCE.createTestObject();
//		to.getRegularContents().add(inner);
//		inner.getRegularContents().add(referee);
//
//		System.out.println("---------");
//		FStoreImpl.INSTANCE.save();
//		injector.getInstance(TestKeyValueStore.class).print(System.out);
//
//		Assert.assertEquals("referee", ((TestObject) FStoreImpl.INSTANCE.getContents().get(0)).getFragmentedContents().get(0)
//				.getCrossReferences().get(0).getName());
//		
//		System.gc();
//		Thread.sleep(100);
//		FStoreImpl.INSTANCE.getFragmentSet().unloadUnreferencedFragments(null, true);
//		
//		to.getFragmentedContents().add(referee);
//		
//		System.out.println("---------");
//		FStoreImpl.INSTANCE.save();
//		injector.getInstance(TestKeyValueStore.class).print(System.out);
//
//		Assert.assertEquals("referee", ((TestObject) FStoreImpl.INSTANCE.getContents().get(0)).getFragmentedContents().get(0)
//				.getCrossReferences().get(0).getName());
//	}
//
//
////  TODO testcases for actual key-value stores should be put somewhere else. This needs an overhaul anyways.
////	@Test
//	public void testLoadFromFragmentedStore() throws Exception {
//		String testTable = "testmodel_1";
//		// initialize the FStore for testing
//		Injector injector = Guice.createInjector(new MyModule() {
//			@Override
//			public void configure(Binder binder) {
//				super.configure(binder);
//				binder.bind(IKeyValueStore.class).to(HBaseKeyValueStore.class);
//			}
//		});
//		FStoreImpl.INSTANCE = injector.getInstance(FStoreImpl.class);
//
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(DOMPackage.eINSTANCE);
//		packages.add(CorePackage.eINSTANCE);
//		packages.add(PrimitiveTypesPackage.eINSTANCE);
//		FStoreImpl.INSTANCE.initialize(packages, testTable, true);
//
//		long start = System.currentTimeMillis();
//		IJavaModel contents = (IJavaModel) FStoreImpl.INSTANCE.getContents().get(0);
//		System.out.println(contents.getJavaProjects().size());
//		TreeIterator<EObject> eAllContents = contents.eAllContents();
//		int count = 0;
//		while (eAllContents.hasNext()) {
//			eAllContents.next();
//			count++;
//		}
//		long end = System.currentTimeMillis();
//		System.out.println("## " + count + " in " + (end - start));
//	}
//
////	@Test
//	public void testLoadOnFragmentedStore() throws Exception {
//		String testTable = "testtable_0";
//		clearDB(testTable);
//		// initialize the FStore for testing
//		Injector injector = Guice.createInjector(new MyModule() {
//			@Override
//			public void configure(Binder binder) {
//				super.configure(binder);
//				binder.bind(IKeyValueStore.class).to(HBaseKeyValueStore.class);
//			}
//		});
//		FStoreImpl.INSTANCE = injector.getInstance(FStoreImpl.class);
//
//		Collection<EPackage> packages = new ArrayList<EPackage>();
//		packages.add(DOMPackage.eINSTANCE);
//		packages.add(CorePackage.eINSTANCE);
//		packages.add(PrimitiveTypesPackage.eINSTANCE);
//		FStoreImpl.INSTANCE.initialize(packages, testTable, false);
//
//		long start = System.currentTimeMillis();
//		ResourceSet rs = new ResourceSetImpl();
//		Resource resource = rs.createResource(URI.createURI("models/set3.xmi"));
//		resource.load(null);
//		EList<EObject> contents = resource.getContents();
//		for (EObject content : new ArrayList<EObject>(contents)) {
//			FStoreImpl.INSTANCE.addContent(content);
//		}
//
//		contents = FStoreImpl.INSTANCE.getContents();
//		FStoreImpl.INSTANCE.save();
//		Assert.assertTrue(contents.size() == 1);
//		long end = System.currentTimeMillis();
//
//		System.out.println("## " + contents.size() + " in " + (end - start));
//	}
//
//	@Override
//	public void complexJObjectTest() throws Exception {
//		// TODO in this context I get either a StackOverflowError or NullPointerException, weird		
//		//super.complexJObjectTest();
//	}
//
//	public static void main(String args[]) throws Exception {
//		FragTests.setUp();
//		FragTests instance = new FragTests();
//		instance.registerPackages();
//		instance.testLoadFromFragmentedStore();
//	}
}
