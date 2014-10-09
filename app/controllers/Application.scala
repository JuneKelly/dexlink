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

  val registrationForm = Form(
    mapping(
      "email" -> nonEmptyText(minLength = 2, maxLength = 64),
      "password1" -> nonEmptyText(minLength = 8),
      "password2" -> nonEmptyText(minLength = 8)
    )(RegistrationData.apply)(RegistrationData.unapply)
  )

  def register = Action {
    Ok(views.html.register(registrationForm))
  }

  def registerPost = Action { implicit request =>
    Logger.info("registration form submitted")

    registrationForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("Registration Errors")
        Ok(views.html.register(formWithErrors))
      },
      registrationData => {
        Redirect("/")
      }
    )
  }
}

case class RegistrationData(
  email: String,
  password1: String,
  password2: String
)
