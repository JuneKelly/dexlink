package models.user


import java.util.UUID
import com.github.nscala_time.time.Imports._


case class UserAccount(
  val id: String,
  val pass: String,
  val created: DateTime = DateTime.now.toDateTime,
  val updated: DateTime = DateTime.now.toDateTime
)
