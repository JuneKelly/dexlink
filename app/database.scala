package database


import java.util.UUID
import com.mongodb.casbah.Imports._
import org.joda.time.LocalDateTime
import com.mongodb.casbah.commons.conversions.scala._
import models.user.UserAccount

// SMELL: surely there is a better way of handling all this
trait Persistence {
  RegisterConversionHelpers()
  RegisterJodaLocalDateTimeConversionHelpers()

  var client: MongoClient = null

  // Initialise the database client
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
        case Some(user) => Some(documentToUserAccount(user))
        case None => None
      }
    }

    def exists(id: String): Boolean = {
      !DB.userAccountCollection.findOneByID(id).isEmpty
    }

    def save(user: UserAccount): Boolean = {
      val doc = userAccountToDocument(user)
      val result = DB.userAccountCollection.save(doc)

      return result.getN == 1
    }

  }

  /** Convert MongoDB Document to a UserAccount,
    * Raises an exception if the document is invalid
    */
  def documentToUserAccount(doc: MongoDBObject): UserAccount = {
    val userId = doc.as[String]("_id")
    val userPass = doc.as[String]("pass")
    val userCreated = doc.as[Option[LocalDateTime]]("created")
    val userUpdated = doc.as[Option[LocalDateTime]]("updated")

    // SMELL
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

  /** Convert UserAccount model to MongoDB document
    */
  def userAccountToDocument(user: UserAccount): MongoDBObject = {
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
