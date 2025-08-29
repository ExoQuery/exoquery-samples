package io.exoquery.example

import io.exoquery.capture
import io.exoquery.codegen.model.NameParser
import io.exoquery.generation.Code
import io.exoquery.generation.CodeVersion
import io.exoquery.generation.DatabaseDriver

/**
 * Invoke the ExoQuery entity-generator!
 *
 * This function is a no-op in order to hose the capture.generate call that will be picked up
 * at runtime by the ExoQuery entity-generator. It does not actually need to be called.
 *
 * NOTES: It is good practice to separate your code-generation calls from your main application logic
 * because code-generation happens during compile-time and you want your code-generation call
 * to be able to be compiled reguarless what you are doing in your main application logic.
 *
 * In some rare cases the entire compilation may be so completely hosed (for example if all the record
 * classes are deleted and need to be regenerated) that it will fail in other files not giving the
 * code-generator a change to even be compile in that case, use the `./gradlew compileKotlin -PonlyRegenRecords=true`
 * command to only compile the file with the code-generation call. See the modified compileKotlin task
 * in the build.gradle.kts file for more information.
 */
fun generate() {
capture.generate(
  Code.Entities(
    CodeVersion.Fixed("1.0.0"),
    DatabaseDriver.Postgres("jdbc:postgresql://localhost:25432/postgres"),
    "io.exoquery.example",
    // Since the database-schema is snake_case, we use the SnakeCase parser to convert the table
    // and column names to camelCase when they are generated.
    nameParser = NameParser.SnakeCase
  )
)
}
