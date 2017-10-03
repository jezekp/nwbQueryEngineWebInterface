Web Interface to NWB Query Engine 
==

Introduction
--

[NQB Query Engine](https://github.com/jezekp/NwbQueryEngine) is a tool for searching in data stored in [Neurodata Without Borders](http://www.nwb.org/) data format.
 
Getting started
==

Running Prerequisites
--

* [Installed JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Installed Apache Tomcat 8](https://tomcat.apache.org/download-80.cgi)
* [Installed Maven](https://maven.apache.org/download.cgi)
  

Build
--

run mvn clean package

Configuration
--

* copy src/main/resources/libHDFql.so to a directory accessible by tomcat
* configure JAVA_OPTS=-Djava.library.path=path to a directory with libHDFQL.so
* configure WEB-INF/project.properties

Run
--

* copy target/nwb-query-engine-web.war to the tomcat webapps directory
* start tomcat
* access localhost:8080/nwb-query-engine-web


Frequent problems
--

* JAVA_OPTS is not set correctly
* files.folder in project.properties is not set or is pointing to a non-existing directory or to a directory with insufficient permissions

