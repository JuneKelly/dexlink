package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.user._
import models.user._
import helpers.ViewContext

object Application extends Controller {

  def index = Action { implicit request =>
    Ok(views.html.index(ViewContext(), "Welcome to dexlink!"))
  }

  def aboutPage = Action { implicit request =>
    Ok(views.html.about(ViewContext()))
  }

}
