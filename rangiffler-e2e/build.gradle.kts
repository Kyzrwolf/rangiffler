plugins {
    java
    id("com.netflix.dgs.codegen") version "8.3.0"
}

dependencies {
    // Logs
    testImplementation(libs.logback)
    testImplementation(libs.slf4j)

    // JUnit
    testImplementation(libs.junit)

    // REST
    testImplementation(libs.okhttp)
    testImplementation(libs.okhttp.urlconnection)
    testImplementation(libs.okhttp.logging.interceptor)
    testImplementation(libs.retrofit) {
        exclude(group = "com.squareup.okhttp3")
    }
    testImplementation(libs.retrofit.converter.jackson) {
        exclude(group = "com.squareup.okhttp3")
    }

    // Spring
    implementation(libs.spring.data.commons)
    testImplementation(libs.spring.jdbc)
    testImplementation(libs.spring.crypto)

    // DB / Hibernate
    testImplementation(libs.p6spy)
    testImplementation(libs.hibernate.core)
    testImplementation(libs.mysql)

    // Allure
    testImplementation(libs.allure.attachments)
    testImplementation(libs.allure.junit5) {
        exclude(group = "org.junit.jupiter")
    }
    testImplementation(libs.allure.selenide) {
        exclude(group = "com.codeborne")
    }
    testImplementation(libs.allure.okhttp3)
    testImplementation(libs.allure.grpc)

    // Web
    testImplementation(libs.selenide)

    // Utils
    testImplementation(libs.datafaker)
    testImplementation(libs.commons.io)
    testImplementation(libs.jsr305)
    testImplementation(libs.sql.formatter)
    compileOnly(libs.jakarta.annotation)
    testImplementation("org.apache.commons:commons-lang3:3.14.0")
    testImplementation(libs.atomikos)
    testImplementation(libs.jta.api)
    testImplementation(libs.ashot)
    testImplementation(libs.assertj)
    testImplementation("com.squareup.retrofit2:converter-scalars:3.0.0")

    // lombok
    testCompileOnly(libs.lombok)
    testAnnotationProcessor(libs.lombok)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.generateJava {
    schemaPaths = mutableListOf("${projectDir}/src/test/resources/schema")
    packageName = "io.student.rangiffler.model"
    typeMapping = mutableMapOf(
        "UserConnection" to "org.springframework.data.domain.Page<io.student.rangiffler.model.types.User>",
        "PhotoConnection" to "org.springframework.data.domain.Page<io.student.rangiffler.model.types.Photo>",
        "Date" to "java.time.LocalDateTime"
    )
    generateClient = true
}

sourceSets {
    test {
        java {
            srcDir(layout.buildDirectory.dir("generated/sources/dgs-codegen"))
        }
    }
}

tasks.compileTestJava {
    dependsOn(tasks.generateJava)
}
