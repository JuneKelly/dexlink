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
