package io.exoquery.example

import io.exoquery.capture
import io.exoquery.controller.jdbc.JdbcControllers
import io.exoquery.controller.jdbc.fromConfig
import io.exoquery.jdbc.runOn
import io.exoquery.kmp.pprint
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BasicTest {
  lateinit var controller: JdbcControllers.Postgres

  @BeforeTest
  fun setup() = runBlocking {
    controller = JdbcControllers.Postgres.fromConfig("testPostgresDB")
  }

  @Test
  fun test() = runBlocking {
    val result = query.buildFor.Postgres().runOn(controller)
    assertEquals(
      result.toSet(),
      setOf(
        UserInfo(firstName = "Alice", lastName = "Anderson", role = "admin", organization = "Acme Widgets"),
        UserInfo(firstName = "Bob", lastName = "Baker", role = "member", organization = "Acme Widgets"),
        UserInfo(firstName = "Bob", lastName = "Baker", role = "admin", organization = "Beta Labs"),
        UserInfo(firstName = "Carol", lastName = "Chen", role = "member", organization = "Beta Labs")
      )
    )
  }
}
