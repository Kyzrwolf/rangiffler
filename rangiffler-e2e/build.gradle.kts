plugins {
    java
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
    testImplementation(libs.spring.data.commons)
    testImplementation(libs.spring.jdbc)
    testImplementation(libs.spring.crypto)

    // DB / Hibernate
    testImplementation(libs.p6spy)
    testImplementation(libs.hibernate.core)

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
    testImplementation(libs.javafaker) {
        exclude(group = "org.yaml")
    }
    testImplementation(libs.commons.io)
    testImplementation(libs.jsr305)
    testImplementation(libs.sql.formatter)
    compileOnly(libs.jakarta.annotation)
}
