FROM alpine/git as clone
ENV HOME=/app/nwbQueryEngineWebInterface
WORKDIR /app/nwbQueryEngineWebInterface
RUN git clone https://github.com/jezekp/nwbQueryEngineWebInterface.git

FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app/nwbQueryEngineWebInterface
COPY --from=clone /app/nwbQueryEngineWebInterface /app
RUN mvn package

FROM 		tomcat:8.0.21-jre8
RUN mkdir /Data
COPY --from=build /app/nwbQueryEngineWebInterface/target/nwb-query-engine-web.war /usr/local/tomcat/webapps/
COPY --from=build /app/nwbQueryEngineWebInterface/src/main/resources/libHDFql.so /
COPY		./Docker/setenv.sh /usr/local/tomcat/bin/
RUN		apt-get update && apt-get install -y libgomp1

