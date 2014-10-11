package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index("Welcome to dexlink!"))
  }

  def aboutPage = Action { implicit request =>
    Ok(views.html.about())
  }

  val registrationForm = Form(
    mapping(
      "email" -> nonEmptyText(minLength = 2, maxLength = 64),
      "password1" -> nonEmptyText(minLength = 8),
      "password2" -> nonEmptyText(minLength = 8)
    )(RegistrationData.apply)(RegistrationData.unapply)
  )

  def register = Action { implicit request =>
    Ok(views.html.register(registrationForm))
  }

  def registerPost = Action { implicit request =>
    Logger.info("registration form submitted")

    registrationForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("Registration Errors")
        Ok(views.html.register(formWithErrors))
      },
      formData => {
        if (formData.password1 == formData.password2) {
          UserAccountService.create(formData.email, formData.password1)
          Redirect("/").flashing(
            "success" -> "Account Created"
          )
        } else {
          Logger.info("password missmatch")
          Ok(views.html.register(
            registrationForm.withGlobalError("Passwords do not match"))
          )
        }
      }
    )
  }
}

case class RegistrationData(
  email: String,
  password1: String,
  password2: String
)
