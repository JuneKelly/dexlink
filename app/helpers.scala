package helpers

import play.api.data.FormError
import play.api.i18n.Messages

object ViewHelpers {

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
