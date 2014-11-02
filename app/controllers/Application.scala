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
        val userName = formData.email
        Logger.info(s"Creating user account $userName")
        UserAccountService.create(formData.email, formData.password1)
        Redirect(routes.Application.index).flashing(
          "success" -> "Account Created"
        )
      }
    )
  }

  def login = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

  def loginPost = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

}

object LoginForm {

  case class Data(
    email: String,
    password: String
  )

  val form = Form(
    mapping(
      "email" -> nonEmptyText(minLength = 2, maxLength = 128),
      "password" -> nonEmptyText(minLength = 8))
      (LoginForm.Data.apply)
      (LoginForm.Data.unapply)
  )

}

object RegistrationForm {

  case class Data(
    email: String,
    password1: String,
    password2: String
  )

  val form = Form(
    mapping(
      "email" -> nonEmptyText(minLength = 2, maxLength = 128),
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
