#Etapa 1: Construcción (Build Stage)
#crea una imagen de maven y java 21 para compilar y ejecutar aplicaciones Java
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app
# Copiamos el archvo de dependencias y descargamos lo necesario el pom.xml
COPY pom.xml .
# Copiamos el codigo fuente de la aplicación
COPY src ./src
# Compilamos la aplicación y empaquetamos el archivo JAR
RUN mvn clean package -DskipTests
#FASE 2: Crear una imagen más ligera para ejecutar la aplicación
# Usamos una imagen base de Java 21 para ejecutar la aplicación
FROM eclipse-temurin:21-jdk-alpine
# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app
# Copiamos el archivo .jar generado en la etapa anterior (build)
# Spring Boot genera el .jar en la carpeta 'target'
COPY --from=build /app/target/*.jar app.jar
# Especificamos el puerto en el que la aplicación escuchará
EXPOSE 8080
# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]