# Utiliza una imagen base de OpenJDK 17 en una imagen slim
FROM openjdk:17-jdk-slim

# Copia el archivo JAR principal a /app en el contenedor
COPY build/libs/FunkoExt2-0.0.1-SNAPSHOT.jar /app/tu-aplicacion.jar

# Establece el directorio de trabajo en /app
WORKDIR /app

# Expone el puerto 8080, que probablemente sea el puerto en el que tu aplicación Spring Boot escucha
EXPOSE 8080

# Comando para iniciar la aplicación Spring Boot
CMD ["java", "-jar", "tu-aplicacion.jar"]
