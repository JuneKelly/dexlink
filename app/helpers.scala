package helpers

import play.api.data.FormError
import play.api.i18n.Messages
import play.api.mvc.Request

object FormHelpers {

  type Errors = Seq[FormError]
  type FieldName = Option[String]

  def prettyErrors(
    errors: Errors, fieldName: FieldName= None): Seq[String] = {

    def prettyFormError(error: FormError): String = {
      val field = fieldName match {
        case Some(name) => name;
        case None => error.key.capitalize
      }
      val args = error.args.headOption
      val errorKey = error.message
      val message = Messages(errorKey, args.map(_.toString).getOrElse(""))

      s"$field $message"
    }

    return errors.map(x => prettyFormError(x))
  }

}


object SessionHelpers {

  def currentUser(implicit request: Request[_]): Option[String] = {
    request.session.get("user")
  }

  def userIsLoggedIn(implicit request: Request[_], user: String) = {
    currentUser(request) == user
  }


}

case class ViewContext(implicit request: Request[_]) {
  val currentUser = SessionHelpers.currentUser(request)
}
