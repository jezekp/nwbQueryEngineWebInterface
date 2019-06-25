FROM alpine/git as clone
ENV HOME=/app/nwbQueryEngineWebInterface
WORKDIR /app/nwbQueryEngineWebInterface
RUN git clone https://github.com/jezekp/nwbQueryEngineWebInterface.git


FROM maven:3.5-jdk-8-alpine as build
WORKDIR /app/nwbQueryEngineWebInterface
COPY --from=clone /app/nwbQueryEngineWebInterface /app
COPY		./Docker/project.properties /app/nwbQueryEngineWebInterface/src/main/webapp/WEB-INF
RUN mvn package

#TODO - configure project.properties for docker from docker file
#TODO - add -v option for nwb indexer
FROM 		tomcat
RUN mkdir /Data
COPY --from=build /app/nwbQueryEngineWebInterface/target/nwb-query-engine-web.war /usr/local/tomcat/webapps/
COPY --from=build /app/nwbQueryEngineWebInterface/src/main/resources/libHDFql.so /
COPY		./Docker/setenv.sh /usr/local/tomcat/bin/
RUN		apt-get update && apt-get install -y libgomp1 build-essential checkinstall \
libreadline-gplv2-dev libncursesw5-dev libssl-dev \
libsqlite3-dev tk-dev libgdbm-dev libc6-dev libbz2-dev libffi-dev zlib1g-dev \
&& wget https://www.python.org/ftp/python/3.7.3/Python-3.7.3.tgz \
&& tar xzf Python-3.7.3.tgz \
&& cd Python-3.7.3 \
&& ./configure --enable-optimizations \
&& make altinstall

