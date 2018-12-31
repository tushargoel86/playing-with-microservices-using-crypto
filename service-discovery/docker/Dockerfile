FROM openjdk:8-jdk-alpine
MAINTAINER tushargoel.com
COPY service-discovery-server.jar /opt/service-discovery-server/lib/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/service-discovery-server/lib/service-discovery-server.jar"]
VOLUME /var/lib/service-discovery-server/service-discovery-repo
EXPOSE 8761