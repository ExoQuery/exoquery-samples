# ExoQuery with JDBC

Example of using ExoQuery with the record-generator to do schema-first (i.e. database-first) development.
This example uses a database schema that has some annoying inconsistencies to give you an idea of how the 
ExoQuery Entity Generator works in real-world situations, because let's face it, some real-world schemas 
make us grind our teeth!

The Entities Generator calls ChatGPT `gpt-4o-mini` (the fastest and cheapest OpenAI model) to sanitize 
the names of Tables and Columns producing sane Kotlin Entities classes that are pleasant to work with.


# Instructions

## Running the Example

1. Set your OpenAI API key the .codegen.properties file in the project root:
   ```
   api-key=<your key here>
   ```
   
   > Or alternatively if you already have a environment variable `OPENAI_API_KEY` 
   > You can modify the `LLM.OpenAI()` setting to `LLM.OpenAI(apiKeyEnvVar="OPENAI_API_KEY")` in `JdbcGenerate.kt`.

2. Start the database before compiling and running the example, this is important because ExoQuery
   code-generates the Entities at compile-time.
   ```
   > ./start.sh
   ```
3. Compile code and run the main-class:
   ```
   > ./gradlew run
   ...
   UserInfo(firstName=Alice, lastName=Anderson, role=admin, organization=Acme Widgets)
   UserInfo(firstName=Bob, lastName=Baker, role=member, organization=Acme Widgets)
   UserInfo(firstName=Bob, lastName=Baker, role=admin, organization=Beta Labs)
   UserInfo(firstName=Carol, lastName=Chen, role=member, organization=Beta Labs)
   ```
4. Run the tests:
   ```
   > ./gradlew test
   ...
   io.exoquery.example.BasicTest > test PASSED
   
   BUILD SUCCESSFUL in 3s
   ```
5. When you are finished you can stop the database by running:
   ```
   > ./stop.sh
   ```

## Regenerating the Entities

If you change the database schema, you will need to regenerate the Entities. Do the following:

1. Bump the version of CodeVersion in the Entities block of `JdbcGenerate.kt`.
2. Recompile the `JdbcGenerate.kt` file to regenerate the Entities:
   ```
   > ./gradlew compileKotlin
   ```
3. Modify `JdbcExample.kt` to use the new version of the Entities.
4. Re-run the example and tests.


> Additionally:
> 6. If there is ever a problem where the code is broken and cannot be fixed without
>    regenerating the Entities, run: 
>    ```
>    ./gradlew clean compileKotlin -PonlyRegenEntities=true
>    ```
>    This will clean up just the generated JdbcGenerate.kt code and regenerate the entities.
