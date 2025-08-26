# ExoQuery with JDBC

Example of using ExoQuery with the record-generator to do schema-first (i.e. database-first) development.
This example uses a highly-consistent schema to give you an idea of how the ExoQuery Code Generator works
with a very sane schema. See the `exoquery-sample-codegen-ai` and `exoquery-sample-codegen-complex`
examples to see how to use the ExoQuery Code Generator in situations that represent more messy
real-world situations.

# Instructions

## Running the Example

1. Start the database before compiling and running the example, this is important because ExoQuery
   code-generates the Entities at compile-time.
   ```
   > ./start.sh
   ```
2. Compile code and run the main-class:
   ```
   > ./gradlew run
   ...
   UserInfo(firstName=Alice, lastName=Anderson, role=admin, organization=Acme Widgets)
   UserInfo(firstName=Bob, lastName=Baker, role=member, organization=Acme Widgets)
   UserInfo(firstName=Bob, lastName=Baker, role=admin, organization=Beta Labs)
   UserInfo(firstName=Carol, lastName=Chen, role=member, organization=Beta Labs)
   ```
3. Run the tests:
   ```
   > ./gradlew test
   ...
   io.exoquery.example.BasicTest > test PASSED
   
   BUILD SUCCESSFUL in 3s
   ```
4. When you are finished you can stop the database by running:
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
