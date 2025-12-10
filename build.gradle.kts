import org.gradle.api.tasks.Exec

plugins {
    kotlin("jvm") version "2.2.20" apply false
}

// List of sample project directories (relative to this file)
val samples = listOf(
    "exoquery-sample-codegen",
    "exoquery-sample-codegen-ai",
    "exoquery-sample-codegen-ai-ollama"
)

fun osIsWindows(): Boolean = System.getProperty("os.name").lowercase().contains("win")

fun registerForAllSamples(
    taskName: String,
    description: String,
    gradleArgs: List<String>
) {
    // Aggregate task
    val aggregate = tasks.register(taskName) {
        group = "samples"
        this.description = description
    }

    samples.forEach { dir ->
        val perSample = tasks.register("${taskName}_${dir}", Exec::class.java) {
            group = "samples"
            this.description = "$description ($dir)"
            workingDir = file(dir)
            commandLine = if (osIsWindows()) listOf("cmd", "/c", "gradlew.bat") else listOf("./gradlew")
            args = gradleArgs
            // Forward stdio so progress is visible
            standardOutput = System.out
            errorOutput = System.err
            isIgnoreExitValue = false
        }
        // Make aggregate depend on each per-sample task
        aggregate.configure { dependsOn(perSample) }
    }
}

registerForAllSamples(
    taskName = "cleanAll",
    description = "Run 'clean' in all sample projects",
    gradleArgs = listOf("clean")
)

registerForAllSamples(
    taskName = "buildAll",
    description = "Run 'build' in all sample projects",
    gradleArgs = listOf("build")
)

registerForAllSamples(
    taskName = "testAll",
    description = "Run 'test' in all sample projects",
    gradleArgs = listOf("test")
)

tasks.register("listSamples") {
    group = "samples"
    description = "List all managed sample project directories"
    doLast {
        println("Managed samples:")
        samples.forEach { println(" - $it") }
    }
}
