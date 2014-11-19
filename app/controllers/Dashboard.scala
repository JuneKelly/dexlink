package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import helpers.ViewContext

object Dashboard extends Controller {

  def dashboard = Action { implicit request =>
    Ok(views.html.dashboard(ViewContext()))
  }

}
