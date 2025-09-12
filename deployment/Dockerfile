# Etapa 1: build con Gradle y JDK 21
FROM gradle:9.0.0-jdk21 AS builder
WORKDIR /app

# Copiar todo el proyecto desde el contexto (root del microservicio)
COPY . .

# Dar permisos al wrapper
RUN chmod +x ./gradlew

# Compilar el proyecto
RUN ./gradlew clean build -x test --no-daemon

# Etapa 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el jar generado desde el módulo de aplicación
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar

EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
