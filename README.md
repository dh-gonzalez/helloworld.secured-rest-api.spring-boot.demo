# helloworld.secured-rest-api.spring-boot.demo
Demo of a spring boot application which exposes a secured REST API

## Prerequisites
* JDK 21
* Maven 3.9.8

## Features
* HTTPS
* Exposes an authenticate POST endpoint which authenticate user and provides a JWT token
* Exposes an anonymous non secured Greeting GET endpoint (no token has to be provided)
* Exposes a secured Greeting GET endpoint (token has to be provided)

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

## Generate self-signed certificate
* `keytool -genkeypair -alias helloworld-demo-keystore -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore helloworld-demo-keystore.p12 -validity 3650`
* `password` is the provided password for keystore generation

## Manual build
* `mvn clean package surefire-report:report jacoco:report`
* `docker build . --file src/main/docker/Dockerfile --build-arg JAR_FILE=target/*.jar --tag ghcr.io/dh-gonzalez/helloworld.secured-rest-api.spring-boot.demo:latest`

### Unit tests HTML report and Jacoco code coverage HTML report
* HTML unit tests report is located at `target/reports/surefire.html`
* Code coverage report is located at `target/site/jacoco/index.html`

## Run

### JAR
`java -jar target/helloworld.secured-rest-api.spring-boot.demo-{version}.jar --spring.config.location=file:src/main/resources/ --spring.profiles.active=dev`

### Docker
* `docker run --name helloworld-container -d -p 8443:8443 -v {absolute/path/to/config}:/config -v {absolute/path/to/keystore}:/keystore -e SPRING_CONFIG_LOCATION=/config/ ghcr.io/dh-gonzalez/helloworld.secured-rest-api.spring-boot.demo:latest`

#### Debug
* `docker logs -f helloworld-container`
* `docker exec -it helloworld-container /bin/sh`

## Test

### Swagger UI
* Swagger UI is available at : [Swagger UI]

### Non secured endpoint
* `curl --silent --insecure -X GET https://localhost:8443/api/v1/anonymous/greeting` should answer `{"greeting":"Hello, World!"}`

### Secured endpoint
* Authenticate and receive token
    * `curl --silent --insecure -X POST https://localhost:8443/api/v1/authenticate -H "Content-Type: application/json" -d '{"username": "admin", "password": "admin"}'` should answer `{"token":"the_token","type":"Bearer","expiresInSeconds":3600}`
* Request secured endpoint providing token
    * `curl --silent --insecure -H "Authorization: Bearer the_token" -X GET https://localhost:8443/api/v1/secured/greeting?name=David` should answer `{"greeting":"Hello, David!"}`

## End2End tests
* Start the application locally
* `mvn test -Pe2e`

### End2End tests HTML report
* located at `target\karate-reports\karate-summary.html`

[Swagger UI]: https://localhost:8443/swagger-ui/index.html
[Swagger api-docs]: https://localhost:8443/v3/api-docs
[Spring Actuator Health]: https://localhost:8443/actuator/health
[Karate]: https://github.com/karatelabs/karate
