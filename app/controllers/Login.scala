package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._

object Login extends Controller {

  def login = Action { implicit request =>
    Ok(views.html.login(LoginForm.form))
  }

  def loginPost = Action { implicit request =>
    LoginForm.form.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.login(formWithErrors))
      },
      formData => {
        val userName = formData.email
        val password = formData.password
        UserAccountService.validateCredentials(userName, password) match {
          case Some(userID) =>
            Redirect(routes.Application.index).flashing("success" -> s"Logged in ${userID}")
          case None =>
            Ok(views.html.login(LoginForm.form.withGlobalError("Login failed")))
        }
      }
    )
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
