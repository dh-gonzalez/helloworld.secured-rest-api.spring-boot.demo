FROM docker.io/library/eclipse-temurin:21.0.5_11-jre-alpine

# Create group my_group and create user my_self in it
RUN addgroup --system my_group && adduser --system --ingroup my_group my_self
# For debug purpose
#    && apk update \
#    && apk add --no-cache curl
USER my_self

EXPOSE 8080

WORKDIR /app
ARG JAR_FILE
COPY --chown=my_self:my_group ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]
