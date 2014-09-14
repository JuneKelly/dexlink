package controllers

import play.api._
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Welcome to dexlink!"))
  }

  def aboutPage = Action {
    Ok(views.html.about())
  }
}
