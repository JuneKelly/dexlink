import play.api._
import play.api.mvc._
import database.DB


object Global extends GlobalSettings {

  override def onStart(app: Application) {
    val databaseUri = app.configuration.getString("mongodb.uri")
    if (databaseUri.isEmpty) {
      throw new Exception("No Database Uri")
    } else {
      DB.init(databaseUri.get)
      Logger.info("Dexlink has started...")
    }
  }

  override def onStop(app: Application) {
    Logger.info("Dexlink shutdown...")
  }

}
