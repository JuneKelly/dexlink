package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._
import helpers.ViewContext

object Login extends Controller {

  def login = Action { implicit request =>
    Ok(views.html.login(ViewContext(), LoginForm.form))
  }

  def loginPost = Action { implicit request =>
    LoginForm.form.bindFromRequest.fold(
      formWithErrors => {
        Ok(views.html.login(ViewContext(), formWithErrors))
      },
      formData => {
        val userName = formData.email
        val password = formData.password
        UserAccountService.validateCredentials(userName, password) match {
          case Some(userID) =>
            Redirect(routes.Application.index)
              .flashing("success" -> s"Logged in ${userID}")
              .withSession("user" -> userID)
          case None =>
            Ok(views.html.login(ViewContext(), LoginForm.form.withGlobalError("Login failed")))
        }
      }
    )
  }

  def logout = Action { implicit request =>
    Redirect(routes.Application.index).flashing("success" -> "Logged out").withNewSession
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
