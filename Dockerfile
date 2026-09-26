# Multi-stage build: compile with the Gradle wrapper against a full JDK, then
# run the resulting Spring Boot fat jar on a slim JRE-only runtime image.
#
# Generic on purpose: no database env vars/ports beyond the app's HTTP port are
# assumed here so a later docker-compose setup can wrap this image without
# changes to this file.

FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace

COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN ./gradlew --version

COPY src ./src
RUN ./gradlew clean bootJar --no-daemon

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

RUN addgroup --system recipe && adduser --system --ingroup recipe recipe
COPY --from=build /workspace/build/libs/*.jar app.jar
RUN chown recipe:recipe app.jar
USER recipe

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
