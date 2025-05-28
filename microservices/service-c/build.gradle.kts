import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// DEPENDENCY MANAGEMENT
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    kotlin(libs.plugins.kotlin.jvm.get().pluginId).version(libs.versions.kotlin.plugin)
    kotlin(libs.plugins.kotlin.spring.get().pluginId).version(libs.versions.kotlin.plugin)
}

dependencies {
    // Spring boot
    api(libs.spring.boot.starter.webflux)

    // Spring cloud
    api(libs.spring.cloud.starter.bootstrap)
    api(libs.spring.cloud.stream.binder.kafka)
    api(libs.spring.cloud.stream.binder.kafka.streams)

    // Spring kafka
    api(libs.spring.kafka)

    // Spring data
    api(libs.spring.data.elasticsearch)

    // Kotlin
    api(libs.kotlin.reflect)
    api(libs.kotlin.stdlib.jdk8)
    api(libs.kotlin.coroutines.reactor)
    api(libs.kotlin.reactor.extensions)
    api(libs.kotlin.logging)
    api(libs.kotlin.jackson.module)
    api(libs.reactor.tools)
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}


// BUILD
tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.add("-Xjsr305=strict")
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

tasks.getByName<Jar>("jar") { enabled = false }

java.sourceCompatibility = JavaVersion.VERSION_21

java.targetCompatibility = JavaVersion.VERSION_21
