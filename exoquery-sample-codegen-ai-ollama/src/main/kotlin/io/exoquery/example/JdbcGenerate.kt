package io.exoquery.example

import io.exoquery.codegen.model.LLM
import io.exoquery.codegen.model.NameParser
import io.exoquery.generation.Code
import io.exoquery.generation.CodeVersion
import io.exoquery.generation.DatabaseDriver
import io.exoquery.sql

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
  sql.generate(
    Code.Entities(
      CodeVersion.Fixed("1.0.0"),
      DatabaseDriver.Postgres("jdbc:postgresql://localhost:26432/postgres"),
      "io.exoquery.example",
      // Since the database-schema is snake_case, we use the SnakeCase parser to convert the table
      // and column names to camelCase when they are generated.
      nameParser =
        NameParser.UsingLLM(

          LLM.Ollama(model = "qwen2.5-coder:0.5b"),
          systemPromptTables =
            """ 
            Convert a list of labels to strictly UpperCamelCase and return a list of old-label:new-label.
            Find what english words make sense to convert to upper case based on their semantic meaning.
            
            Example Input:
            <INPUT>
            1)foo_bar_baz
            2)Foobarbaz
            3)one_Two_three
            4)Carentity
            5)youngperson
            6)YoungPerson
            7)original_sales_records
            </INPUT>
            
            Example Output:
            <OUTPUT>
            1)foo_bar_baz:FooBarBaz
            2)Foobarbaz:FooBarBaz
            3)one_Two_three:OneTwoThree
            4)Carentity:CarEntity
            5)YoungPerson:YoungPerson
            6)OldPerson:OldPerson
            7)original_sales_records:OriginalSalesRecords
            </OUTPUT>
            """,
          systemPromptColumns =
            """
            Convert a list of labels to strictly lowerCamelCase and return a list of old-label:new-label.
            Find what english words make sense to convert to upper case based on their semantic meaning.
            
            Example Input:
            <INPUT>
            1)foo_bar
            2)foo_bar_baz
            3)fooBar
            4)Foobarbaz
            5)one_Two_three
            6)Carentity
            7)youngperson
            8)OldPerson
            9)original_sales_records
            </INPUT>
            
            Example Output:
            <OUTPUT>
            1)foo_bar:fooBar
            2)foo_bar_baz:fooBarBaz
            3)fooBar:fooBar
            4)Foobarbaz:fooBarBaz
            5)one_Two_three:oneTwoThree
            6)Carentity:carEntity
            7)youngperson:youngPerson
            8)OldPerson:oldPerson
            9)original_sales_records:originalSalesRecords
            </OUTPUT>
            """
        )
    )
  )
}
