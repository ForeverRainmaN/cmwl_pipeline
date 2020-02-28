package cromwell.pipeline.datastorage.dao.repository.utils

import java.util.UUID

import cromwell.pipeline.datastorage.dto.User.UserEmail
import cromwell.pipeline.datastorage.dto.{ User, UserId }
import cromwell.pipeline.utils.StringUtils

object TestUserUtils {
  val userPassword = "-Pa$$w0rd-"

  def getDummyUser(
    uuid: String = UUID.randomUUID().toString,
    email: UserEmail = "JohnDoe-@cromwell.com",
    password: String = userPassword,
    passwordSalt: String = "salt",
    firstName: String = "FirstName",
    lastName: String = "Lastname",
    active: Boolean = true
  ): User = {
    val passwordHash = StringUtils.calculatePasswordHash(password, passwordSalt)
    User(
      UserId(uuid),
      s"JohnDoe-$uuid@cromwell.com",
      passwordHash,
      passwordSalt,
      firstName,
      lastName,
      None,
      active
    )
  }
}