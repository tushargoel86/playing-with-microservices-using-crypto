FROM openjdk:8-jdk-alpine
MAINTAINER tushargoel.com
COPY config-server.jar /opt/spring-cloud/lib/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/spring-cloud/lib/config-server.jar"]
VOLUME /var/lib/spring-cloud/config-repo
EXPOSE 9999