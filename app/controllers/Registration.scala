package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._
import helpers.ViewContext

object Registration extends Controller {

  def register = Action { implicit request =>
    Ok(views.html.register(ViewContext(), RegistrationForm.form))
  }

  def registerPost = Action { implicit request =>
    Logger.info("Registration form submitted")

    RegistrationForm.form.bindFromRequest.fold(
      formWithErrors => {
        Logger.info("Registration Errors")
        Ok(views.html.register(ViewContext(), formWithErrors))
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
