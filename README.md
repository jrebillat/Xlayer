# XLayer quick documentation

## Concept
The idea behind XLayer is to enable everyone to parse an XML file describing a set of java objects creation and assembly along with method calls and variable management.
It is widely inspired by the XAML and FXML features.

## before parsing document
parsing of a document is done in Java code. A _Run_ class is available for running an application by parsing a file, but you may find it useful to parse a document from your own applicatoion.

In this case you are able to specify some information before parsing the file.

### Registring packages and classes
By default, all classes from the jars in the class path are already registered. The ones that may not be registered are those in the system libraries (java.*, javax.* and so on).
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

### Resource parsing
It is possible to parse a Resource from a jar file instead of a simple file, with one of the following methods :
```java
List<String> Manager.parseResource(String xmlString);
List<String> Manager.parseResource(Object root, Reader reader);
```

### Direct Run

You may also just use the net.alantea.xlayer.Run class :
```bash
java (add your class path) net.alantea.xlayer.Run "path/to/myfile.xml"
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
The root element may contain one or more namespaces definitions. This definitions will associate XML prefixes to package names :
```xml
<xlayer xmlns:tst='net.alantea.xlayer.test.raw'>
</xlayer>
```
=> the package _net.alantea.xlayer.test.raw_ will be associated with the _tst:_ prefix. If used in file
it will be registered. It will be ignored before being used in an element definition.

It is also possible to register a package from inside the file, using the _package_ processing instruction :
```xml
<?package 'my.package.path'?>
```

You may also register packages from your code before parsing the XML file, using :
```java
void Manager.addPackage(String pack);
void Manager.addClass(String pack);
```

## What can be an element ?
A element can be :
+ An object class to be created
+ a field in the parent object
+ A method applying to the parent object
+ A list of elements
+ A value (a String or a simple type)
+ A constant from a class
+ Xlayer keywords

## Tree registration

As any XML element should be registered somewhere in the parent object, there is a procedure to search how to put the result of the element parsing inside its parent. 

If the container for the object is another object, then a search through the class hierarchy (all steps for the class of the object, then same for superclass, and again until _Object_ class) will be made :
+ search for a method "myObject(_class_ object)" in container
+ search for a method "setMyObject(_class_ object)" in container
+ search for a method "addMyObject(_class_ object)" in container
+ search for a field named _myObject_ in container with assignable _class_
+ search for a method "get_MyObject_s()" in container and an then search a "add(_class_ object)" in the returned type.
+ search for a method "add(_class_ object)" in container

The result is also put in a list of all elements in parent - used for methods, see below.

## Object creation

When the element is an object class, it must be a bean, with an empty constructor. The complete definition for a object is :
```xml
<myObject _class='classname' _set='asVariable' (attributes)>
   whatever
</myObject>
```
The "_class" attribute is optional. If not given, the used class will be "MyObject", searched in all the registered packages.
You can also, when creating it, put the object in a variable using the ___set attribute. This will act like the _variable_ keyword.

All attributes, except for ___class and ___set, will be considered as fields in the element and parsed as a field element.

## Fields
Fields are manage in the same way as objects (in fact they are objects). The major thing to know there is that even private fields may be set this way.

## Methods
Methods are a little tricky to use, as they may need parameters. When the element keyword is identified as a method, all elements inside it will be set in a list, and then given to the method when invoking it. They must be given in the correct order.

An exception is, if there is no corresponding method signature, to search for a method with acorrect name, but excepting just a list as its single parameter. Then this method is called will one argument : the list containing all parameters.

The value of the method element is, of course, the value returned from invocation.

## Lists
A list is simply a collection of objects. It is implicitly built for each element by collecting the value of each child. But if you have to give a method a specific parameter as a list, there is a keyword for that - see below.

## Values
Simple values are easy to set : they are just text in their parent. The string is parsed to transform it into the correct type for their parent's need. An example :
```xml
<Label>
  <text>Hello World !</text>
</Label>
```
Sometimes, it is not possible to do it so simply, as for list creation. You can also encapsulate it in an element of its type :
```xml
<integer>22</integer>
<String>Toto</String>
```

## Constants
Constants from a class may be inserted when needed, using the Xlayer keyword _constant_. The syntax is :
```xml
<constant class='the.class' name='CONSTANT_NAME'/>
```
The _class_ attributes indicates the class to parse for constant. The _name_ attribute is the name of the constant.

It may be used like in :
```xml
<constant class='javax.swing.JFrame' name='EXIT_ON_CLOSE' />
```

## Xlayer keywords

### xlayer
_xlayer_ is a placeholder for global container, artefact needed by XML and not always useful here.

### include
_include_ allows to include another file or resource inside the currently parsing process. The work is done in the same parser, thus elements parsed from the outer parsing are available to inner parsing, which elements will later be available in the upper parsing.
```xml
<include path='innertest.xml'/>
```

### list
lists may be created with the "list" keycode:
```xml
<list>
 <integer>1</integer>
 <integer>2</integer>
 <integer>3</integer>
 <integer>4</integer>
</list>
```

### variable
You can create and use variable. A variable may contain any object or a simple type value.
creating is like :
```xml
<variable name='myname'>whatever XML tree</variable>
```

using a variable anywhere is :
```xml
<myobject _variable='myname'/>
```

if needed, you may get a variable value in your code after parsing :
```java
Object myObject = Manager.getVariable("myObject");
```

### script
_script_ allows to insert a script as placeholder for any interface. Anywhere where some kind of interface may be replaced with a script (like for ActionListener in awt) you may replace the call to an instance with the keyword.
The syntax is :
```xml
<script as='my.interface.to.replace'>
```

An example :
```xml
<addActionListener>
   <script as="ActionListener">
      print('Hello world');
   </script>
</addActionListener>
```

Scripts are based on the java-included javascript engine, so they may access all java classes, even the Xlayer Manager. please see the documentation on Java's ScriptEngine and its Javascript implementation for more information.
They may also get Xlayer-defined variables using :
```javascript
   var Manager = Java.type("net.alantea.xlayer.Manager");
   var val = parseInt(Manager.getVariable('myvariable'));
```

The two special javascript variables _methodName_ and _methodArguments_ are containing respectively the method name called and the list of the given argument objects.
The last value in script is returned from the method if it is compatible with the type excepted in the interface

## Example
A simple example of a swing frame, runnable with the `Run`class, is :
```xml
<?xml version="1.0"?>
<xlayer xmlns:swg='javax.swing'>
	<?package java.awt.event?>
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
