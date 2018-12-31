FROM openjdk:8-jdk-alpine
MAINTAINER tushargoel.com
COPY gateway-server.jar /opt/gateway-server/lib/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/gateway-server/lib/gateway-server.jar"]
VOLUME /var/lib/gateway-server/gateway-server-repo
EXPOSE 8765