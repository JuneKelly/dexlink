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

  def register = Action { implicit request =>
    Ok(views.html.register(RegistrationForm.form))
  }

  def registerPost = Action { implicit request =>
    Logger.info("Registration form submitted")

    RegistrationForm.form.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("Registration Errors")
        Ok(views.html.register(formWithErrors))
      },
      formData => {
        Logger.info(s"Creating user account $formData.email")
        UserAccountService.create(formData.email, formData.password1)
        Redirect("/").flashing(
          "success" -> "Account Created"
        )
      }
    )
  }

}

object RegistrationForm {

  case class Data(
    email: String,
    password1: String,
    password2: String
  )

  val form = Form(
    mapping(
      "email" -> nonEmptyText(minLength = 2, maxLength = 64),
      "password1" -> nonEmptyText(minLength = 8),
      "password2" -> nonEmptyText(minLength = 8))
      (RegistrationForm.Data.apply)
      (RegistrationForm.Data.unapply)
      .verifying(
        "Passwords do not match",
        fields =>
        fields match {
          case userData => RegistrationForm.validatePasswords(
            userData.password1,
            userData.password2
          )
        }
      )
  )

  def validatePasswords(pass1: String, pass2: String): Boolean = {
    return pass1 == pass2
  }

}
