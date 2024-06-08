FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

# Accept the service account JSON file as a build argument
ARG FIREBASE_SERVICE_ACCOUNT
COPY ${FIREBASE_SERVICE_ACCOUNT} /app/firebase-adminsdk.json

# Compilar el proyecto y generar el archivo jar
RUN mvn package -DskipTests

# Etapa de construcción de la imagen
FROM openjdk:17

# Copiar el archivo jar generado en la etapa de build a la carpeta /app
COPY --from=build /app/target/panoptimize-0.0.1-SNAPSHOT.jar /app/app.jar

# Exponer el puerto 8080 para que se pueda acceder a la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando se inicie el contenedor
CMD [ "java", "-jar", "/app/app.jar"]