FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

# Compilar el proyecto y generar el archivo jar
RUN mvn package

# Etapa de construcción de la imagen
FROM openjdk:17

# Copiar el archivo jar generado en la etapa de build a la carpeta /app
COPY --from=build /app/target/panoptimize-0.0.1-SNAPSHOT.jar /app/app.jar

# Agregar variables de entorno desde actions
ARG MYSQL_HOST
ARG MYSQL_USER
ARG MYSQL_PASSWORD
ARG SIMULATION_URL

ENV MYSQL_HOST=$MYSQL_HOST
ENV MYSQL_USER=$MYSQL_USER
ENV MYSQL_PASSWORD=$MYSQL_PASSWORD
ENV SIMULATION_URL=$SIMULATION_URL

# Exponer el puerto 8080 para que se pueda acceder a la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD [ "java", "-jar", "/app/app.jar"]