package services.user

import models.user.UserAccount
import database.UserAccountStorage


object UserAccountService extends UserAccountStorage {

  def get(id: String): Option[UserAccount] = {
    val result = backend.get(id)
    result match {
      case None => None
      case Some(user) => Some(user)
    }
  }

}
