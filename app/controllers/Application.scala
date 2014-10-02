package controllers

import play.api._
import play.api.mvc._
import services.user._
import models.user._

object Application extends Controller {

  def index = Action {
    val user = UserAccountService.get("shanek")
    // val user = None
    Ok(views.html.index("Welcome to dexlink!", user))
  }

  def aboutPage = Action {
    Ok(views.html.about())
  }
}
