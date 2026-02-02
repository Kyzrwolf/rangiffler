plugins {
    java
    id("org.springframework.boot") version "3.5.10"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.netflix.dgs.codegen") version "8.3.0"
}

group = "io.student"
version = "0.0.1-SNAPSHOT"
description = "rangiffler-api"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

val flywayVersion: String by extra

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-graphql")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.tailrocks.graphql:graphql-datetime-spring-boot-starter:6.0.0")
    implementation ("org.flywaydb:flyway-core:$flywayVersion")
    implementation ("org.flywaydb:flyway-mysql:$flywayVersion")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    runtimeOnly("com.mysql:mysql-connector-j:9.5.0")
}

//tasks.generateJava {
//    schemaPaths.add("${projectDir}/src/main/resources/graphql")
//    packageName = "io.student.model"
//    generateClient = false
//}

tasks.withType<Test> {
    useJUnitPlatform()
}
