package io.exoquery.example

import io.exoquery.capture
import io.exoquery.controller.jdbc.JdbcControllers
import io.exoquery.controller.jdbc.fromConfig
import io.exoquery.example.public.OrgAccountMembers
import io.exoquery.example.public.OrganizationAccounts
import io.exoquery.example.public.UserProfiles
import io.exoquery.jdbc.runOn
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(val firstName: String, val lastName: String, val role: String, val organization: String)

val query = capture.select {
  val org = from(Table<OrganizationAccounts>())
  val member = join(Table<OrgAccountMembers>()) { it.orgId == org.id }
  val user = join(Table<UserProfiles>()) { it.id == member.userId }
  where { org.isActive }
  UserInfo(user.firstName, user.lastName, member.roleName, org.orgName)
}

fun main() = runBlocking {
  val ctx = JdbcControllers.Postgres.fromConfig("testPostgresDB")
  val result = query.buildFor.Postgres().runOn(ctx)
  println(result.joinToString("\n"))
}
