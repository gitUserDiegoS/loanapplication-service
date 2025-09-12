# stage 1: build with Gradle y JDK 21
FROM gradle:8.14.3-jdk-21-and-24-noble AS builder
WORKDIR /app

# copy the whole project
COPY . .

# give permission
RUN chmod +x ./gradlew

# compile project
RUN ./gradlew clean build -x validateStructure -x test --no-daemon

# stage 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# copy generated jar
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
