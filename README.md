Web Interface to NWB Query Engine 
==

Introduction
--

[NWB Query Engine](https://github.com/jezekp/NwbQueryEngine) is a tool for searching in data stored in [Neurodata Without Borders](http://www.nwb.org/) data format.

A [demo](http://eeg.kiv.zcu.cz:8080/nwb-query-engine-web/) is available.
 
Getting started
==

Running Prerequisites
--

* [Installed JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Installed Apache Tomcat 8](https://tomcat.apache.org/download-80.cgi)
* [Installed Maven](https://maven.apache.org/download.cgi)

* or [Docker](https://www.docker.com/) instead


Configuration
--

* configure WEB-INF/project.properties
  

Build
==

run mvn clean package

Deploy
==

Tomcat
--
* copy src/main/resources/libHDFql.so to a directory accessible by tomcat
* configure JAVA_OPTS=-Djava.library.path=path to a directory with libHDFQL.so
* copy target/nwb-query-engine-web.war to the tomcat webapps directory
* start tomcat

Or Docker
--

* docker build -t nwb-query-engine-web -f Docker/Dockerfile .
* docker run -p 8080:8080 -v &lt;host data directory path&gt;:/Data nwb-query-engine-web


Use
==
* access localhost:8080/nwb-query-engine-web



Frequent problems
==

* JAVA_OPTS is not set correctly
* files.folder in project.properties is not set or is pointing to a non-existing directory or to a directory with insufficient permissions
* parameter -v (Docker) is not set correctly

