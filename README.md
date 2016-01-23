# XLayer quick documentation

## Concept
The idea behind XLayer is to enable everyone to parse an XML file describing a set of java objects creation and assembly along with method calls and variable management.

## before parsing document
parsing of a document is done in Java code. A _Run_ class is available for running an application by parsing a file, but you may find it useful to parse a document from your own applicatoion.

In this case you are able to specify some information before parsing the file.

### Registring packages and classes
You may register packages or specific classes before parsing the XML file, using :
```java
void Manager.addPackage(String pack);
void Manager.addClass(String pack);
```
### Defining variables
You may provide variable definitions, to be used in XML or in scripts :
```java
void Manager.setVariable(String name, Object content);
```

## Parsing
The goal is in fact to parse some XML stuff and generate some instances, call methods and do some work.

### File parsing
The parsing of a file is done with one of the following methods :
```java
List<String> Manager.parseFile(String filePath);
List<String> Manager.parseFile(Object root, String filePath);
List<String> Manager.parse(Object root, Reader reader);
```
In all cases, the content of the file or reader is evaluated as an XML file with the syntax described below. The difference is in the parameters. You or not give a _root_ object. If given, parsing will begin as if it was already parsing the root object. If the `root` object is not given, or is `null` then it will be ignored.

The returned value is a list of all found errors. 

### String parsing
It is possible to parse a String instead of a file, with one of the following methods :
```java
List<String> Manager.parse(String xmlString);
List<String> Manager.parse(Object root, Reader reader);
```

### Direct Run

You may also just use the net.alantea.xlayer.Run class :
```bash
java _add your class path_ net.alantea.xlayer.Run _path/to/myfile.xml_
```

## Document Syntax

### Document root

As for all XML files, there must be a root element.
```xml
<root>
whatever
</root>
```

This root element may be the keycode _xlayer_, that will be ignored - but is necessary for XML.
```xml
<xlayer>
whatever
</xlayer>
```

### Packages definition
The root element may contain namespaces definitions. This definitions will associate XML prefixes to package names :
```xml
<xlayer xmlns:tst='net.alantea.xlayer.test.raw'>
</xlayer>
```
=> the package _net.alantea.xlayer.test.raw_ will be associated with the _tst:_ prefix. If used in file
it will be registered. It will be ignored before being used in an element definition.

It is also possible to register immediately a package in the file, using the _package_ element :
```xml
<package name='my.package.path'/>
```

You may also register packages before parsing the XML file, using :
```java
void Manager.addPackage(String pack);
void Manager.addClass(String pack);
```



For each xml element, the parser will search for a field, a method (unless attribute "_class" is given, see below)
then an object. Think that using method is only useful when they have multiple parameters, as it is
possible to do object creation and setting in one step (see below).

If the container is a method, the created contained objects will be added to the list of parameters for it.
Note that methods may not be nested.

When this is an object, it must be a bean (with an empty constructor) :
<myObject _class='classname'></myObject>
The "_class" attribute is optional. If not given, the searched class will be "MyObject".
You can also put the object in a variable using the "_set" attribute.

If the container for the object is another object, then a search through the class hierarchy 
(all steps for a class, then same for superclass) will be made :
1- search for a method "myObject(Myobject object)" in container
2- search for a method "setMyObject(Myobject object)" in container
3- search for a method "addMyObject(Myobject object)" in container
4- search for a field named "myObject" in container
5- search for a method "getMyObjects()" in container and an "add(MyObject object)" on the returned type.

You can create and use variable. A variable may contain an object or a simple type.
creating is like :
<variable name='myname'>whatever XML tree</variable>

using a variable anywhere is like :
<myobject _variable='myname'/>

if needed, you may get a variable value after parsing :

Object Manager.getVariable(String name);

you can create a simple value just by settings its value :
<myValue>36</myValue>
or by specifying its type - necessary to build lists, see below :
<integer>22</integer> or <String>Toto</String>

lists may be created with the "list" keycode:
<list>
 <integer>1</integer>
 <integer>2</integer>
 <integer>3</integer>
 <integer>4</integer>
</list>

Anywhere where some kind of interface may be replaced with a script (like for ActionListener in awt) you may
replace the call to an instance with the keyword <script as='my.interface.to.replace'>, like :
<addActionListener>
   <script as="ActionListener">
      print('Hello world');
   </script>
</addActionListener>

Scripts are based on java-included javascript engine, so theymay access all java classes, even Manager and
get Xlayer-defined variables using :
   var Manager = Java.type("net.alantea.xlayer.Manager");
   var val = parseInt(Manager.getVariable('myvariable'));
The two javascriptvariables "methodName" and "methodArguments" are containing the method name called and the
list of arguments.
The last value in script is returned from the method if it is compatible with the type excepted in the interface

# Example
a simple example of a swing frame, runnable with the `Run`class, is :
```xml
<?xml version="1.0"?>
<xlayer xmlns:swg='javax.swing'>
	<package name="java.awt.event" />
	<swg:jFrame title="HelloWorldSwing">
		<defaultCloseOperation>
			<constant class='javax.swing.JFrame' name='EXIT_ON_CLOSE' />
		</defaultCloseOperation>
		<jPanel>
			<jLabel _put='myLabel'>
				<text>Hello World too...</text>
			</jLabel>
		<variable name='intvar'>42</variable>
			<jButton name='mybutton1'>
				<text>Hello World too...</text>
				<addActionListener>
					<script as="ActionListener">
					  if (methodName == "actionPerformed")
					  {
					    var Manager = Java.type("net.alantea.xlayer.Manager");
					    var val = parseInt(Manager.getVariable('intvar'));
					    print('Yes, hello world ' + val);
					    Manager.setVariable('intvar', val + 1);
					    var label = Manager.getVariable('myLabel');
					    label.setText('Yes, hello world ' + val);
					  }
					</script>
				</addActionListener>
			</jButton>
		</jPanel>
		<pack />
		<visible>true</visible>
	</swg:jFrame>
</xlayer>
```
