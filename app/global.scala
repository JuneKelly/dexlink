import play.api._
import play.api.mvc._
import play.filters.csrf._
import services.user.UserAccountService


object Global extends WithFilters(CSRFFilter()) with GlobalSettings {

  override def onStart(app: Application) {
    Lifecycle.seedUserAccounts()
    Logger.info("Dexlink has started...")
  }

  override def onStop(app: Application) {
    Logger.info("Dexlink shutdown...")
  }

}


object Lifecycle {

  def seedUserAccounts(): Unit = {
    if(!UserAccountService.exists("admin@example.com")) {
      UserAccountService.create("admin@example.com", "password")
    }
    if(!UserAccountService.exists("test@example.com")) {
      UserAccountService.create("test@example.com", "password")
    }
  }

}
