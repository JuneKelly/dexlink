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
      "password2" -> nonEmptyText(minLength = 8))
      (RegistrationData.apply)
      (RegistrationData.unapply)
      .verifying(
        "Passwords do not match",
        fields =>
        fields match {
          case userData => validateForm(
            userData.password1,
            userData.password2
          )
        }
      )
  )

  def register = Action { implicit request =>
    Ok(views.html.register(registrationForm))
  }

  def registerPost = Action { implicit request =>
    Logger.info("Registration form submitted")

    registrationForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("Registration Errors")
        Ok(views.html.register(formWithErrors))
      },
      formData => {
        UserAccountService.create(formData.email, formData.password1)
        Redirect("/").flashing(
          "success" -> "Account Created"
        )
      }
    )
  }

  def validateForm(pass1: String, pass2: String): Boolean = {
    return pass1 == pass2
  }

}

case class RegistrationData(
  email: String,
  password1: String,
  password2: String
)
