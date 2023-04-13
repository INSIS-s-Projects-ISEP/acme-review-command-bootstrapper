# Dependencies Cache and Compilation
FROM maven:3.9.0-eclipse-temurin-17-alpine AS maven-build
WORKDIR /usr/src/review-command-bootstrapper
COPY ./pom.xml ./
COPY ./src ./src
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Run
FROM eclipse-temurin:17-jre-alpine AS app-runtime
WORKDIR /app
COPY --from=maven-build /usr/src/review-command-bootstrapper/target/*.jar ./review-command-bootstrapper.jar
ENTRYPOINT ["java", "-jar", "review-command-bootstrapper.jar"]