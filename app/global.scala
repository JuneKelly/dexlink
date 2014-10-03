import play.api._
import play.api.mvc._


object Global extends GlobalSettings {

  override def onStart(app: Application) {
    Logger.info("Dexlink has started...")
  }

  override def onStop(app: Application) {
    Logger.info("Dexlink shutdown...")
  }

}
