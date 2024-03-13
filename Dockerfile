# Stage 1 - packaging jar
FROM maven:3.6.3-openjdk-17 AS maven
RUN mkdir job4j_tracker
WORKDIR /job4j_tracker
COPY . .
RUN mvn install -Dmaven.test.skip=true

# Stage 2 - running project
FROM openjdk:17.0.2-jdk
WORKDIR /job4j_tracker
COPY --from=maven /job4j_tracker/target/tracker.jar app.jar
CMD java -jar app.jar