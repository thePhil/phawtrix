import org.gradle.api.tasks.testing.logging.TestLogEvent.*

group = "ch.phildev."
version = "0.0.3-SNAPSHOT"
description = "spring-phawtrix"

repositories {
    mavenCentral()
}

plugins {
    java
    `maven-publish`
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"

}

java {
    sourceCompatibility = JavaVersion.VERSION_14
    targetCompatibility = JavaVersion.VERSION_14
}

val mqttVersion = "1.2.0"
val guavaVersion = "29.0-jre"
val webfluxDocVersion = "1.3.9"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.google.code.gson:gson")
    implementation("org.springdoc", "springdoc-openapi-webflux-ui", webfluxDocVersion)
    implementation("com.hivemq", "hivemq-mqtt-client", mqttVersion)
    implementation("com.hivemq", "hivemq-mqtt-client-reactor", mqttVersion)
    implementation("com.google.guava", "guava", guavaVersion)
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

}

tasks.test {
    useJUnitPlatform()
    jvmArgs("--enable-preview")
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events(PASSED,SKIPPED,FAILED)
    }
}


tasks.withType<JavaCompile> {
    options.compilerArgs.add("--enable-preview")
}
tasks.withType<JavaExec>{
    jvmArgs("--enable-preview")
}


