# ExoQuery with AI using Ollama

Example of using ExoQuery with the record-generator to do schema-first (i.e. database-first) development.
This example talks to a locally-running Ollama instance in order to convert a otherwise
nasty and inconvenient database schema into a pleasant-to-use Kotlin API. The default model is `qwen2.5-coder:0.5b`
which is a very small (0.5 billion parameter) model that can run on just about any reasonably modern laptop.
This makes the enclosed approach accessible to just about anyone, even folks working behind a
extremely restrictive set of corporate firewalls.

Right how (as of Aug 2024) AI is in this really strange state. On the one hand, many employers completely
block access to any external LLM services, on the other hand laptops that have the vram to run even 1-2B models
with decent token-per-second speeds are not quite mainstream. That means that a developer integrating client-side AI
can't really expect their users to have access to even the smallest reliable models (i.e. 7B and up). That means
that a library like ExoQuery needs to carefully prompt-engineer around the limitations of the tiniest models.
Also these tiny models are extremely finnicky and don't support features like structural (i.e. json) outputs, 
so the prompt-engineering must rely on some kind of custom format whose own description can fit into the 
tiny token limits that laptops running this tiny models can handle.

I have had a limited amount of success with qwen2.5-coder:0.5b meeting the requirements above
but even after trying dozens of prompts it makes frequent mistakes and the output requires hand edits.
Fortunately ExoQuery code-generator will not regenerate the Entities when the `CurrentVersion.kt` file is 
up to date with the version specified in the `Code.Entities block`. This means that you can hand-edit
any badly-generated code and the code-generator will not overwrite your fixes unless you bump the version.

When going through rapid iteration of the prompt, you can force regeneration by either bumping the version
or running `./gradlew clean compileKotlin -PonlyRegenEntities=true` which will just regenerate the Entities
and leave the rest of the code alone (the 'clean' only cleans the generated Entities code).

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
