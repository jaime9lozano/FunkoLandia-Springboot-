plugins {
    java
    id("org.springframework.boot") version "3.1.4"
    id("io.spring.dependency-management") version "1.1.3"
}

group = "jaime"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Dependencias de Spring Web for HTML Apps y Rest
    implementation("org.springframework.boot:spring-boot-starter-web")

    // Spring Data JPA par SQL
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Spring Data JPA para MongoDB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Cache
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // Validación
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Websocket
    implementation("org.springframework.boot:spring-boot-starter-websocket")

    // Thyemeleaf
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    // Spring Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // H2 Database - Para desarrollo
    implementation("com.h2database:h2")
    // PostgreSQL - Para producción
    implementation("org.postgresql:postgresql")

    // Para usar con jackson el controlador las fechas: LocalDate, LocalDateTime, etc
    // Lo podemos usar en el test o en el controlador, si hiciese falta, por eso está aquí
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Para pasar a XML los responses, negocacion de contenido
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml")

    // Para manejar los JWT tokens
    // JWT (Json Web Token)
    implementation("com.auth0:java-jwt:4.4.0")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")


    // Dependencias para Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // Test Spring Security
    testImplementation("org.springframework.security:spring-security-test")

    // MongoDB para test, pero no es necesario, usamos sus repositorios
    // testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo.spring31x:4.9.3")

    // Extras para web
    // Bootstrap
    implementation("org.webjars:bootstrap:4.6.2")

}

tasks.withType<Test> {
    useJUnitPlatform()
}
