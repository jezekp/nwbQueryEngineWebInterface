FROM 		tomcat:8.0.21-jre8

MAINTAINER 	Petr Jezek

RUN mkdir /Data
COPY 		./target/nwb-query-engine-web.war /usr/local/tomcat/webapps/
COPY 		./src/main/resources/libHDFql.so /
COPY		./Docker/setenv.sh /usr/local/tomcat/bin/
RUN		apt-get update && apt-get install -y libgomp1

