# helloworld.secured-rest-api.spring-boot.demo
Demo of a spring boot application which exposes a secured REST API

## Prerequisites
* JDK 21
* Maven 3.9.8

## Features
* Exposes a non secured and a secured HTTP GET Helloworld method

## Technical choices

### Application
* Auto-executable Spring Boot application
* REST API is implemented with reactive Spring WebFlux framework
* REST API documentation is provided at [Swagger UI]

### Unit tests
* JUnit5 and Mockito unit tests for execution eficiency
* Everything in the service to be tested has to be mocked

### End2End tests
* End2End tests implemented with [Karate]

## Manual build
* `mvn clean package surefire-report:report jacoco:report`
* `docker build --build-arg JAR_FILE=target/*.jar -t ghcr.io/dh-gonzalez/helloworld.secured-rest-api.spring-boot.demo:latest .`

### Unit tests HTML report and Jacoco code coverage HTML report
* HTML unit tests report is located at `target/reports/surefire.html`
* Code coverage report is located at `target/site/jacoco/index.html`

## Run

### JAR
`java -jar target/helloworld.secured-rest-api.spring-boot.demo-{version}.jar --spring.config.location=file:src/main/resources/`

### Docker
* `docker run --name helloworld-container -d -p 8080:8080 -v {absolute/path/to/config/files}:/config -e SPRING_CONFIG_LOCATION=/config/ ghcr.io/dh-gonzalez/helloworld.secured-rest-api.spring-boot.demo:latest`

### Debug
* `docker logs -f helloworld-container`
* `docker exec -it helloworld-container /bin/sh`

## Test

* Swagger UI is available at : [Swagger UI]
* `curl --silent http://localhost:8080/api/v1/anonymous/greeting` should answer `Hello, World!`
* `curl --silent http://localhost:8080/api/v1/secured/greeting?name=David` should answer `Hello, David!`

## End2End tests
* Start the application locally
* `mvn test -Pe2e`

### End2End tests HTML report
* located at `target\karate-reports\karate-summary.html`

## TODOs
* Switch to HTTPS
* Secure endpoint greeting

[Swagger UI]: http://localhost:8080/swagger-ui/index.html
[Karate]: https://github.com/karatelabs/karate
