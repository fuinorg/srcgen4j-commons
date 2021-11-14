# srcgen4j-commons
Source code generation for Java (Commons)

[![Build Status](https://jenkins.fuin.org/job/srcgen4j-commons/badge/icon)](https://jenkins.fuin.org/job/srcgen4j-commons/)
[![Coverage Status](https://sonarcloud.io/api/project_badges/measure?project=org.fuin.srcgen4j%3Asrcgen4j-commons&metric=coverage)](https://sonarcloud.io/dashboard?id=org.fuin.srcgen4j%3Asrcgen4j-commons)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.fuin.srcgen4j/srcgen4j-commons/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.fuin.srcgen4j/srcgen4j-commons/)
[![LGPLv3 License](http://img.shields.io/badge/license-LGPLv3-blue.svg)](https://www.gnu.org/licenses/lgpl.html)
[![Java Development Kit 11](https://img.shields.io/badge/JDK-11-green.svg)](https://openjdk.java.net/projects/jdk/11/)


## Versions
- 0.4.3 (or later) = **Java 11** before namespace change from 'javax' to 'jakarta'
- 0.4.2 (or previous) = **Java 8**

## Background
A model driven approach requires almost always generating some code (model to text) or other output. 
This framework provides an easy way to setup multiple parsers and generators in a single configuration.
It allows building a pipeline built on those parsers and generators.

![Pipeline](https://raw.github.com/fuinorg/srcgen4j-commons/master/doc/srcgen4j-pipeline.png)

The pipeline is configured using a single XML configuration file:
```xml
<srcgen4j-config
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xmlns="http://www.fuin.org/srcgen4j/commons/0.4.3">
    
    <variables />
    <projects />
    <parsers />
    <generators />
    
</srcgen4j-config>
```
There is currently a Maven plugin ([srcgen4j-maven](https://github.com/fuinorg/srcgen4j-maven/)) that executes the pipeline during a build or on manually.
An Eclipse plugin is planned, but not yet available. There are some predefined parsers and generators abailable in the ([srcgen4j-core](https://github.com/fuinorg/srcgen4j-core/)) project.

## Variables
The variables section allows defining globally visible variables.
```xml
<variables>
    <variable name="path" value="/var/tmp" />
    <variable name="sub" value="${path}/mypath" />
    <variable name="esacpes" value="\r\n\t" />
    <variable name="res" url="classpath:header.txt" encoding="utf-8" />
</variables>
```
A variable definition is either a simple name/value combination or an URL that points to the content.
Any content type that Java is capable to handle can be used. Additionally the content type "classpath"
allows reading files from your classpath. There is a special variable called *rootDir* that is always 
available and points to the root directory. In case of a Maven build this is the directory where you
executed the 'mvn' command.

Variables can be overwritten in the sub-sections:
```xml
<variables>
    <variable name="a" value="/var/tmp" />
</variables>
<parsers>
    <variable name="a" value="${a}/parsers1" />
</parsers>
```
The result is only visible inside the defining section.

## Projects
A project is used to define the folders where the generated output can be placed.
```xml
<projects>
  <project name="myprj" path="." maven="false">
        <folder name="doc" path="doc" create="true" override="true" clean="true" />
  </project>
</projects>
```
A Maven directory structure is assumed by default. This can be disabled with *maven="false"*.
```xml
<project name="myprj" path=".">
    <!-- It's NOT necessary to add the following! It's just to show the default folder structure. -->
    <folder name="mainJava" path="src/main/java" create="false" override="false" clean="false" />
    <folder name="mainRes" path="src/main/resources" create="false" override="false" clean="false" />
    <folder name="genMainJava" path="src-gen/main/java" clean="true" cleanExclude="\..*" />
    <folder name="genMainRes" path="src-gen/main/resources" create="true" clean="true" />
    <folder name="testJava" path="src/test/java" create="false" override="false" clean="false" />
    <folder name="testRes" path="src/test/resources" create="false" override="false" clean="false" />
    <folder name="genTestJava" path="src-gen/test/java" create="true" clean="true" />
    <folder name="genTestRes" path="src-gen/test/resources" create="true" clean="true" />
</project>
```
A folder is defined by a name that is unique within the project and a path inside the project's directory.
```xml
<folder name="mainJava" 
        path="src/main/java" 
        create="false"
        override="false" 
        overrideExclude="[A\.java|B\.java]"
        clean="false"
        cleanExclude="\..*" />
```
Other attributes that indluence the generation process are:
* *create* Create the directory if it does not exist
* *override* Override existing files in that directory
* *overrideExclude* Regular expression to exclude some files from being overridden.
* *clean* All files in the directory will be deleted bevore new ones are created.
* *cleanExclude* Regular expression to exclude some files from being deleted.

## Parsers
The parsers section defines one or more parsers that are used to create the input models.
```xml
<parsers>
    <parser name="dddParser" class="org.fuin.srcgen4j.core.xtext.XtextParser">
        <config>
            <xtext:xtext-parser-config modelPath="src/main/domain" modelExt="ddd"
              setupClass="org.fuin.dsl.ddd.DomainDrivenDesignDslStandaloneSetup" />
        </config>
    </parser>
</parsers>
```
Every parser has a unique name and a full qualified class name that is used to instantiate the parser (using the default constructor).
Some parsers might require a special configuration that can be added in the config section.

## Generators
The generator section defines one or more generators that use the input of a parser and write their output to one of the projects.
```xml
<generators>
    <generator name="dddGenerator" class="org.fuin.srcgen4j.core.emf.EMFGenerator" parser="dddParser" project="current">
        <config>
            <!-- Generator specific configuration -->
            <emf:artifact-factory artifact="AbstractAggregate" class="org.fuin.dsl.ddd.gen.aggregate.AbstractAggregateArtifactFactory" />
        </config>
        <artifact name="AbstractAggregate" folder="genMainJava" />
        <!-- More artifact definition -->
    </generator>
</generators>
```
Every generator has a unique name and a full qualified class name that is used to instantiate the generator (using the default constructor).
Some generators might require a special configuration that can be added in the config section.
A generator creates one or more artifacts that are written to the configured folder of the project.

- - - - - - - - -

## Snapshots
Snapshots can be found on the [OSS Sonatype Snapshots Repository](http://oss.sonatype.org/content/repositories/snapshots/org/fuin "Snapshot Repository"). 

Add the following to your .m2/settings.xml to enable snapshots in your Maven build:

```xml
<repository>
    <id>sonatype.oss.snapshots</id>
    <name>Sonatype OSS Snapshot Repository</name>
    <url>http://oss.sonatype.org/content/repositories/snapshots</url>
    <releases>
        <enabled>false</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
    </snapshots>
</repository>
```
