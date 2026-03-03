# Etapa 1: Compilación (Build)
FROM maven:3.9.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# Copiamos el pom y descargamos dependencias (aprovechando el caché de Docker)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código fuente y generamos el .jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución (Runtime)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copiamos solo el archivo .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
