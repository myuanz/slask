package example

import fun.myuan.slask.Utils.md5HashString

object Utils

case class User(email: String, password: String)

case class EmailError(message: String) extends Exception(message)

object Users {
  var users: Map[String, User] = Map() // 相当于email是索引

  def addUser(email: String, password: String): Unit = {
    email match {
      case i if !users.contains(i) => users = users ++ Map(email -> User(email, md5HashString(password)))
      case _ => throw EmailError("Email already exists")
    }
  }

  def checkPassword(email: String, password: String): Boolean = {
    email match {
      case i if users.contains(i) => users(email).password == md5HashString(password)
      case _ => throw EmailError("Email does not exist")
    }
  }

}