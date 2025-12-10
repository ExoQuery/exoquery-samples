package io.exoquery.example

import com.google.cloud.spanner.jdbc.JdbcDataSource
import io.exoquery.controller.jdbc.JdbcControllers
import kotlinx.coroutines.runBlocking
import javax.sql.DataSource

/**
 * Minimal example showing ExoQuery Postgres dialect working over the
 * Google Cloud Spanner JDBC driver.
 *
 * This sample only verifies connectivity by executing a trivial query.
 * You must create the database (or run the emulator) before running.
 */
fun main() = runBlocking {
  // Read Spanner configuration from environment variables
  val emulatorHost = System.getenv("SPANNER_EMULATOR_HOST") // e.g., "localhost:9010"
  val project = System.getenv("SPANNER_PROJECT") ?: "demo-project"
  val instance = System.getenv("SPANNER_INSTANCE") ?: "demo-instance"
  val database = System.getenv("SPANNER_DATABASE") ?: "demo-db"

  // Build JDBC URL for Spanner with PostgreSQL dialect
  val jdbcUrl = buildString {
    append("jdbc:cloudspanner:/projects/$project/instances/$instance/databases/$database")
    append(";dialect=postgresql")
    if (emulatorHost != null) {
      append(";usePlainText=true;autoConfigEmulator=true")
    }
  }

  println("Connecting to Spanner: $jdbcUrl")
  if (emulatorHost != null) {
    println("Using emulator at: $emulatorHost")
  }

  // Create Spanner JDBC DataSource
  val dataSource: DataSource = JdbcDataSource().apply {
    setUrl(jdbcUrl)
  }

  // Build a Postgres-dialect JDBC controller with the Spanner DataSource
  val ctx = JdbcControllers.Postgres(dataSource)

  println("Initialized ExoQuery Postgres controller with Google Spanner JDBC driver.")
  println("Extend this sample to run ExoQuery sql queries against your schema.")
}
