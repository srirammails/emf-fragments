# PROJECT MOVED #
We moved the code-base, wiki, and issue tracking to [github](http://github.com/markus1978/emf-fragments)

# Synopsis #
EMF fragments is a Eclipse Modeling Framework (EMF) persistence layer for distributed data stores like NoSQL databases (e.g. MongoDB, Hadoop/HBase) or distributed file systems.

# What Problem Does It Solve? #
The [Eclipse Modeling Framework (EMF)](http://www.eclipse.org/emf) is designed to programmatically create, edit, and analyze software models. It provides generated type-safe APIs to manipulate models based on a schema (i.e. metamodel). This is similar to XML and XML-schemas with JAX-like APIs. EMF works fine as long as you use it for software models and your models fit into main memory. If you use EMF for different data, e.g. sensor-data, geo-data, social-data, you run out of main memory soon and thinks become a little bit more complicated.

Why would I use EMF for this kind of data anyways? EMF provides very good generated APIs, generated GUI tools to visualize data, and a series of strong model transformation languages. All things one can apply to structured data. Data in EMF is described through metamodels similar to XML schemas or entity-relationship diagrams.

To use larger models in EMF, we need something to persist models that does not require us to load complete models into memory. Existing solutions include ORM mappings (i.e. eclipse's CDO). These solutions have three drawbacks:

  1. ORM mappings store data slowly because data is indexed and stored very fine grained
  1. ORM mappings are slow when structures are traversed because data is loaded piece by piece even though it is used by larger aggregates
  1. SQL databases are not so easily distributed

EMF fragments is designed to store large object-oriented data models (typed, labeled, bidirectional graphs) efficiently and scalable. EMF fragments builds on key-value stores. EMF fragments emphasize on fast storage of new data and fast navigation of persisted models. The requirements for this framework come from storing and analyzing large ammounts of sensor data in real-time.

# How Does EMF Fragments Work? #
EMF fragments are different from frameworks based on object relatational mappings (ORM) like Connected Data Objects (CDO). While ORM mappings map single objects, attributes, and references to databae entries, EMF fragments map larger chunks of a model (fragments) to URIs. This allows to store models on a wide range of distributed data-stores inlcuding distributed file-systems and key-value stores (think NoSQL databases like MongoDB or HBase). This also prepares EMF models for cloud computing paradigms such as Map/Reduce.

![http://www.markus-scheidgen.de/wp-content/uploads/2012/06/Screen-Shot-2012-06-18-at-18.08.48-1024x493.png](http://www.markus-scheidgen.de/wp-content/uploads/2012/06/Screen-Shot-2012-06-18-at-18.08.48-1024x493.png)

The EMF fragments framework allows automated transparent background framgmentation of models. Clients designate types of references at which models are fragmented. This allows to control fragmentation without the need to trigger it programatically. Fragments are managed automatically: when you create, delete, move, edit model elements new fragments are created and elements are distributed among those fragments on the fly. Fragments (i.e. resources) are identified by URIs. The framework allows to map URIs to (distributed) data-stores (e.g. NoSql databases or distributed file systems).

# How Is EMF Fragments Used? #
Using EMF fragments is simple if you are used to [http:www.eclipse.org/emf/ EMF]. You create EMF metamodels as usual, e.g. with ecore. You generate APIs and tools as usual using normal genmodels but with three specific parameters.

  1. You have to configure your genmodels to use _reflective feature delegation_.
  1. You have to use a specific base class: `FObjectImpl`
  1. You have to enable _Containment Proxies_

You use the generated APIs and tools as usual. EMF fragments provide a specific `ResourceSet`implementation. called `FragmentedModel`. `Resource`s are managed automatically in the background and you do not have to create, load or unload them manually (as you would have to without EMF fragments).

To actually have your models fragmented, you need to annotate those reference features in your meta-model that you want to cross fragment borders. This is only possible for containment references. When an object is added to a container via such a fragmentation reference feature, a new resource will be created automatically and the new contained will be automatically put into that new fragment.

EMF fragments provides an abstract interface to map resources (fragments) URIs to a physical storage. An implementation for Apache HBase and an in-memory test implementation is provided.

# Hello World Example #
This example is part of the de.hub.emffrag.tests eclipse project. You can find it within the sources of emf-fragments.
For this Hello World example we use a very simple meta-model:

![http://emf-fragments.googlecode.com/files/example-meta-model-figure.png](http://emf-fragments.googlecode.com/files/example-meta-model-figure.png)

The following code demonstrates how to initialize emf-fragments, to create a model, and how to traverse a model:
```
// necessary if you use EMF outside of a running eclipse environment
EPackage.Registry.INSTANCE.put(TestModelPackage.eINSTANCE.getNsURI(), TestModelPackage.eINSTANCE);
EPackage.Registry.INSTANCE.put(EcorePackage.eINSTANCE.getNsURI(), EcorePackage.eINSTANCE);
Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new XMIResourceFactoryImpl());    

// use the next lines to use HBase as IKeyValueStore
// Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put("hbase", new XMIResourceFactoryImpl());
// IKeyValueStore keyValueStore = new HBaseKeyValueStore();

// use the next lines to use an in-memory-test IKeyValueStore
IKeyValueStore keyValueStore = new TestKeyValueStore();

// initialize your model
FragmentedModel model = new FragmentedModel(keyValueStore, "Hello World", TestModelPackage.eINSTANCE);

// create a root object and add it to the model
Container testContainer = TestModelFactory.eINSTANCE.createContainer();
model.addContent(testContainer);

// create the rest of your model as usual
Contents testContents = TestModelFactory.eINSTANCE.createContents();
Contents testFragmentedContents = TestModelFactory.eINSTANCE.createContents();

testContents.setValue("Hello Old World!");       
testFragmentedContents.setValue("Hello New World!");

testContainer.getContents().add(testContents);
testContainer.getFragmentedContents().add(testFragmentedContents);

// call save to force save of cached and unsaved parts of your model before exiting the JVM
model.save();

System.out.println("Key value store contents: ");
System.out.println("Entry 0:\n" + keyValueStore.getTable("Hello World", false).get("0"));
System.out.println("Entry 1:\n" + keyValueStore.getTable("Hello World", false).get("1"));

// to read a model initialize the environment as before
// initialize your model
FragmentedModel readModel = new FragmentedModel(keyValueStore, "Hello World", TestModelPackage.eINSTANCE);

// navigate the model as usual
System.out.println("Iterate results: ");
TreeIterator<EObject> allContents = readModel.getContents().get(0).eAllContents();
while (allContents.hasNext()) {
	EObject next = allContents.next();
	if (next instanceof Contents) {
		System.out.println(((Contents)next).getValue());
	}
}
```

The result should be something like this:
```
Key value store contents: 
Entry 0:
<?xml version="1.0" encoding="ASCII"?>
<tm:Container xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:tm="http://hu-berlin.de/sam/emfhbase/testmodel">
  <contents value="Hello Old World!"/>
  <fragmentedContents href="1#/"/>
</tm:Container>

Entry 1:
<?xml version="1.0" encoding="ASCII"?>
<tm:Contents xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:tm="http://hu-berlin.de/sam/emfhbase/testmodel" value="Hello New World!"/>

Iterate results: 
Hello Old World!
Hello New World!
```

As you can see, the object added to the `fragmentedContents` reference was stored in its own fragment. The object added to the normal `contents` reference was stored in the same fragment as its container. The `fragmentedContents` reference was annotated with _de.hub.emfhbase: Fragmentation->true_, the reference `contents` was not.