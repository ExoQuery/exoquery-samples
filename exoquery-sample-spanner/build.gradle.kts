import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "2.2.20"
  id("application")
}

repositories {
  mavenCentral()
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  api("io.exoquery:exoquery-runner-jdbc:2.0.0.PL")
  // Google Cloud Spanner JDBC driver (PostgreSQL dialect compatible)
  implementation("com.google.cloud:google-cloud-spanner-jdbc:2.14.4")
  // HikariCP for connection pooling
  implementation("com.zaxxer:HikariCP:5.1.0")

  testImplementation(kotlin("test"))
}

tasks.withType<Test>().configureEach {
  testLogging {
    events("passed", "skipped", "failed")
    showStandardStreams = true
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}

application {
  mainClass.set("io.exoquery.example.JdbcExampleKt")
}

tasks.jar {
  manifest {
    attributes["Main-Class"] = "io.exoquery.example.JdbcExampleKt"
  }
}
