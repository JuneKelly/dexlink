package helpers

import play.api.data.FormError
import play.api.i18n.Messages

object ViewHelpers {

  def prettyFormError(error: FormError): String = {
    val field = error.key.capitalize
    val args = error.args.headOption
    val errorKey = error.message
    val message = Messages(errorKey, args.map(_.toString).getOrElse(""))

    s"$field $message"
  }

  def prettyErrors(errors: Seq[FormError]): Seq[String] = {
    errors.map(x => prettyFormError(x))
  }

}
