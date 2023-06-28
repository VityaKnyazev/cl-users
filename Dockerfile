FROM openjdk:17
COPY . /usr/src/userApp
WORKDIR /usr/src/userApp

EXPOSE 8083

RUN microdnf install findutils

CMD ["/bin/bash", "-c", "./gradlew update;cd build/libs;java -jar users-0.0.1-SNAPSHOT.jar"]
