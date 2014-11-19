package database


import java.util.UUID
import com.mongodb.casbah.Imports._
import org.joda.time.LocalDateTime
import com.mongodb.casbah.commons.conversions.scala._
import models.user.UserAccount
import play.api.Play


trait Persistence {
  RegisterConversionHelpers()
  RegisterJodaLocalDateTimeConversionHelpers()

  private var clientConnection: Option[MongoClient] = None

  def client(): MongoClient = {
    clientConnection match {
      case Some(connection) => connection
      case None =>
        val uri = Play.current.
          configuration.getString("mongodb.uri").
          getOrElse("")
        val connection = MongoClient(MongoClientURI(uri))
        clientConnection = Some(connection)
        return connection
    }
  }

}


object DB extends Persistence {

  def database(): MongoDB = {
    client()("dexlink")
  }

  def collection(collectionName: String): MongoCollection = {
    database()(collectionName)
  }

  def userAccountCollection(): MongoCollection = {
    collection("user_account")
  }
}


/** Storage backend for UserAccount objects
  */
trait UserAccountStorage {

  val backend = UserAccountStorageBackend

  object UserAccountStorageBackend {

    def get(id: String): Option[UserAccount] = {
      val doc = DB.userAccountCollection.findOneByID(id)
      doc match {
        case Some(user) => Some(fromDocument(user))
        case None => None
      }
    }

    def exists(id: String): Boolean = {
      !DB.userAccountCollection.findOneByID(id).isEmpty
    }

    def insert(user: UserAccount): Boolean = {
      if (exists(user.id)) {
        false
      } else {
        val doc = toDocument(user)
        val result = DB.userAccountCollection.insert(doc)
        return result.getN == 1
      }
    }
  }

  def fromDocument(doc: MongoDBObject): UserAccount = {
    val userId = doc.as[String]("_id")
    val userPass = doc.as[String]("pass")
    val userCreated = doc.as[Option[LocalDateTime]]("created")
    val userUpdated = doc.as[Option[LocalDateTime]]("updated")

    if (userCreated.isEmpty || userUpdated.isEmpty) {
      throw new InvalidUserAccountException(
        s"UserAccount $userId invalid")
    } else {
      UserAccount(
        id = userId,
        pass = userPass,
        created = userCreated.get.toDateTime,
        updated = userUpdated.get.toDateTime
      )
    }
  }

  def toDocument(user: UserAccount): MongoDBObject = {
    MongoDBObject(
      "_id" -> user.id,
      "pass" -> user.pass,
      "created" -> user.created,
      "updated" -> user.updated
    )
  }

  class InvalidUserAccountException(msg: String)
      extends RuntimeException(msg)

}
