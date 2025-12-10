ExoQuery + Google Cloud Spanner (PostgreSQL dialect)

This sample wires ExoQuery's PostgreSQL dialect to the Google Cloud Spanner JDBC driver and provides a Docker-based emulator setup with a sample schema.

What’s included
- Docker Compose to run the Spanner Emulator and auto-create a PostgreSQL‑dialect database.
- A sample schema compatible with Spanner’s PostgreSQL dialect: `schema.spanner.pg.sql`.
- Minimal Kotlin app that initializes an ExoQuery Postgres controller over the Spanner JDBC driver.

Prerequisites
- Docker and Docker Compose
- JDK 17

Quick start (Emulator)
1) Start the emulator and initialize schema
   - From the repo root:
     ```
     docker compose -f exoquery-sample-spanner/docker-compose.yaml up -d spanner-emulator
     docker compose -f exoquery-sample-spanner/docker-compose.yaml up spanner-init
     ```
   - This will:
     - Start the emulator on port 9010.
     - Create a project/instance/database (defaults: project=demo-project, instance=demo-instance, database=demo-db).
     - Apply `schema.spanner.pg.sql` to the database.

2) Export environment variables for the sample app
   - In a new terminal, set the emulator host and database coordinates. These must match what Compose used (defaults shown):
     ```bash
     export SPANNER_EMULATOR_HOST=localhost:9010
     export SPANNER_PROJECT=demo-project
     export SPANNER_INSTANCE=demo-instance
     export SPANNER_DATABASE=demo-db
     ```
   - Alternatively, you can provide a full JDBC URL (overrides parts above):
     ```bash
     export SPANNER_JDBC_URL="jdbc:cloudspanner:/projects/${SPANNER_PROJECT}/instances/${SPANNER_INSTANCE}/databases/${SPANNER_DATABASE};usePlainText=true;autoConfigEmulator=true;dialect=postgresql"
     ```

3) Run the sample application
   - From the repo root:
     ```
     ./gradlew :exoquery-sample-spanner:run
     ```
   - You should see a confirmation that the ExoQuery Postgres controller is initialized with the Spanner JDBC driver.

Project files
- Docker Compose: `exoquery-sample-spanner/docker-compose.yaml`
- Schema (DDL): `exoquery-sample-spanner/schema.spanner.pg.sql`
- App config: `exoquery-sample-spanner/src/main/resources/application.conf` (uses key `spanner`)
- Main app: `exoquery-sample-spanner/src/main/kotlin/io/exoquery/example/JdbcExample.kt`

Notes on the schema
- Uses simple supported types and explicit `BIGINT` primary keys.
- Avoids `SERIAL`; if you need auto-generated IDs, manage them in application code or via custom sequences when supported.
- Foreign keys are added without cascading actions for emulator compatibility.

Troubleshooting
- If the app cannot connect, ensure `SPANNER_EMULATOR_HOST` is exported in the same shell where you run Gradle and points to `localhost:9010`.
- If you changed project/instance/db names in Compose, update your env vars to match.
- To view emulator logs:
  ```
  docker logs spanner-emulator
  ```

Cleanup
- Stop services: `docker compose -f exoquery-sample-spanner/docker-compose.yaml down`
- Remove volumes/state as needed with `-v` (emulator is in-memory but containers still exist):
  ```
  docker compose -f exoquery-sample-spanner/docker-compose.yaml down -v
  ```

Next steps
- Extend `JdbcExample.kt` to run ExoQuery `sql` queries against your schema.
- If you move to a real Spanner instance, unset `SPANNER_EMULATOR_HOST` and configure `SPANNER_PROJECT/INSTANCE/DATABASE` (or a full `SPANNER_JDBC_URL` without emulator flags). The driver will use your Application Default Credentials.
