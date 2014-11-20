package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._
import helpers.{ViewContext, SessionHelpers}

object User extends Controller {

  def account = Action { implicit request =>
    Ok(views.html.userAccount(ViewContext()))
  }
}
