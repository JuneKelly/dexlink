package services.user


import models.user.UserAccount
import database.UserAccountStorage
import com.github.t3hnar.bcrypt._


object UserAccountService extends UserAccountStorage {

  def get(id: String): Option[UserAccount] = {
    val result = backend.get(id)
    result match {
      case None => None
      case Some(user) => Some(user)
    }
  }

  def exists(id: String) = backend.exists(id)

  def create(id: String, pass: String): Option[UserAccount] = {
    if (exists(id)) {
      return None
    }
    val user = UserAccount(id=id, pass=pass.bcrypt)
    val result = backend.insert(user)
    if (result == true) {
      Some(user)
    } else {
      None
    }
  }
}
