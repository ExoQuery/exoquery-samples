// Composite aggregator for ExoQuery sample projects (Option C)
// This keeps each sample as an independent build while providing a unified
// entry point for IDE import and CI via the root Gradle wrapper.

rootProject.name = "exoquery-samples"

// Include each sample as an independent build
includeBuild("exoquery-sample-codegen")
includeBuild("exoquery-sample-codegen-ai")
includeBuild("exoquery-sample-codegen-ai-ollama")
includeBuild("exoquery-sample-spanner")
