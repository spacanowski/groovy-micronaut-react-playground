FROM adoptopenjdk/openjdk11-openj9:jdk-11.0.1.13-alpine-slim
COPY build/libs/ad-etl-*-all.jar ad-etl.jar
EXPOSE 8080
CMD java -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar ad-etl.jar