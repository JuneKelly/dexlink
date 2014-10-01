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
        case Some(user) =>
          println(user.getClass)
          val userId = user.as[String]("_id")
          val userPass = user.as[String]("pass")
          // val userCreated = user.as[Option[java.util.Date]]("created")
          // val userUpdated = user.as[Option[java.util.Date]]("updated")
          val userCreated = user.as[Option[LocalDateTime]]("created")
          val userUpdated = user.as[Option[LocalDateTime]]("updated")

          if (userCreated.isEmpty || userUpdated.isEmpty) {
            return None
          }

          Some(
            UserAccount(
              id = userId,
              pass = userPass,
              created = userCreated.get.toDateTime,
              updated = userUpdated.get.toDateTime
            )
          )
        case None => None
      }
    }

  }
}
