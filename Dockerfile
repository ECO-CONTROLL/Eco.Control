# Etapa de build
FROM gradle:8.7-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle build -x test --no-daemon

# Etapa de execução
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 10000

CMD ["java", "-jar", "app.jar"]
