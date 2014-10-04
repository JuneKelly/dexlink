package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to dexlink!"))
  }

  def aboutPage = Action {
    Ok(views.html.about())
  }

  def register = Action {
    Ok(views.html.register(None))
  }
}

case class RegistrationData(
  userid: String,
  passwordOne: String,
  passwordTwo: String
)
