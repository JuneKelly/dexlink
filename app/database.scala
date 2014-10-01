package database


import java.util.UUID
import com.mongodb.casbah.Imports._
import org.joda.time.LocalDateTime
import com.mongodb.casbah.commons.conversions.scala._

import models.user.UserAccount


trait Persistence {
  RegisterJodaLocalDateTimeConversionHelpers()

  var client: MongoClient = null

  def init(uri: String) {
    client = MongoClient(MongoClientURI(uri))
  }
}


object DB extends Persistence {

  def database(): MongoDB = {
    client("dexlink")
  }

  def collection(collectionName: String): MongoCollection = {
    database()(collectionName)
  }

}


trait UserAccountStorage {

  val backend = UserAccountStorageBackend

  object UserAccountStorageBackend {

    def get(id: String): Option[UserAccount] = {
      val coll = DB.collection("user_account")
      val doc = coll.findOneByID(id)
      doc match {
        case Some(user) => Some(documentToUserAccount(user))
        case None => None
      }
    }

  }

  def documentToUserAccount(doc: MongoDBObject): UserAccount = {
    val userId = doc.as[String]("_id")
    val userPass = doc.as[String]("pass")
    val userCreated = doc.as[Option[LocalDateTime]]("created")
    val userUpdated = doc.as[Option[LocalDateTime]]("updated")

    if (userCreated.isEmpty || userUpdated.isEmpty) {
      throw new InvalidUserAccountException(
        s"UserAccount $userId invalid"
      )
    }

    UserAccount(
      id = userId,
      pass = userPass,
      created = userCreated.get.toDateTime,
      updated = userUpdated.get.toDateTime
    )
  }

  class InvalidUserAccountException(msg: String)
      extends RuntimeException(msg)

}
