import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "2.2.0" // Currently the plugin is only available for Kotlin-JVM
  id("io.exoquery.exoquery-plugin") version "2.2.0-2.0.0.PL"
  kotlin("plugin.serialization") version "2.2.0"
  id("application")
}

repositories {
    mavenCentral()
    mavenLocal()
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
  implementation("org.postgresql:postgresql:42.7.0")

  implementation("io.zonky.test:embedded-postgres:2.0.7")
  implementation("io.zonky.test.postgres:embedded-postgres-binaries-linux-amd64:16.2.0")

  // OPTIONAL: Just for testing
  testImplementation(kotlin("test"))
  testImplementation(kotlin("test-common"))
  testImplementation(kotlin("test-annotations-common"))
}

// OPTIONAL: Make tests show in the build log even when they pass
tasks.withType<Test>().configureEach {
  testLogging {
    events("passed", "skipped", "failed")
    showStandardStreams = true
    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
  }
}

fun onlyRegenEntities() =
  providers.gradleProperty("onlyRegenEntities")
    .map { if (it.isBlank()) true else it.toBoolean() }
    .orElse(false)

// Use the commaond `./gradlew clean compileKotlin -PonlyRegenEntities=true` if you need
// to only regenerate the entities and skip compiling everything else. (ONLY clean the JdbcGenerate.kt file)
// This is useful as a last resort e.g. if you have deleted all the generated entities and need to
// get the back from the DB schema.
tasks.named<KotlinCompile>("compileKotlin") {
  // If we are only regenerating entities, then only compile the JdbcGenerate.kt file
  if (onlyRegenEntities().get()) {
    println("----------- ONLY REGENERATING RECORDS -----------")
    val srcDir = project.projectDir.toPath().resolve("src").toAbsolutePath().toString()
    setSource(files("${srcDir}/main/kotlin/io/exoquery/example/JdbcGenerate.kt"))
  }

  // Otherwise build normally
}

exoQuery {
  codegenDrivers.add("org.postgresql:postgresql:42.7.3")
  // If we are only rebuilding the entities, then always force regeneration
  forceRegen = onlyRegenEntities().get()
  enableCodegenAI = true
}

application {
  mainClass.set("io.exoquery.example.JdbcExampleKt")
}

tasks.jar {
  manifest {
    attributes["Main-Class"] = "io.exoquery.example.JdbcExampleKt"
  }
}
