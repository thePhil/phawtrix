import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

group = "ch.phildev."
version = "0.0.3-SNAPSHOT"
description = "spring-phawtrix"

repositories {
    mavenCentral()
}

plugins {
    java
    idea
    `maven-publish`
    id("org.springframework.boot") version "2.3.4.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("com.github.ben-manes.versions") version "0.33.0"
}

java {
    sourceCompatibility = JavaVersion.VERSION_15
    targetCompatibility = JavaVersion.VERSION_15
}
idea {
    module {
        isDownloadJavadoc = false
        isDownloadSources = true
    }
}

val mqttVersion = "1.2.1"
val guavaVersion = "29.0-jre"
val webfluxDocVersion = "1.4.6"
val jetBrainsAnnotationVersion = "20.1.0"
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-oauth2-core")

    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("com.google.code.gson:gson")
    implementation("org.springdoc", "springdoc-openapi-webflux-ui", webfluxDocVersion)
    implementation("com.hivemq", "hivemq-mqtt-client", mqttVersion)
    implementation("com.hivemq", "hivemq-mqtt-client-reactor", mqttVersion)
    implementation("com.google.guava", "guava", guavaVersion)
    implementation("org.jetbrains", "annotations", jetBrainsAnnotationVersion)

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.assertj:assertj-core")
    testImplementation("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testAnnotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

}

tasks.test {
    useJUnitPlatform()
//    jvmArgs("--enable-preview")
    testLogging {
        showExceptions = true
        showStandardStreams = true
        events(PASSED, SKIPPED, FAILED)
    }
}

//tasks.withType<JavaCompile> {
//    options.compilerArgs.add("--enable-preview")
//}
//tasks.withType<JavaExec> {
//    jvmArgs("--enable-preview")
//}

tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

springBoot {
    buildInfo()
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}
