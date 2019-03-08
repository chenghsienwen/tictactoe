package scalasummerschool.tictactoe.util

import cats.data.Validated
import cats.syntax.validated._

object Validation {

  type ErrorsOr[A] = Validated[List[String], A]

  def checkNonEmpty(fieldName: String, text: String): ErrorsOr[String] = {
    if (text.nonEmpty) {
      text.valid[List[String]]
    } else {
      List(s"Given $fieldName should not be empty").invalid[String]
    }
  }

  def checkLength(text: String): ErrorsOr[String] = {
    if (text.length >= 3) {
      text.valid[List[String]]
    } else {
      List("The username and password should be at least 3 characters long").invalid[String]
    }
  }

  def checkUpperLowerChar(text: String): ErrorsOr[String] = {
    val charList = text.toList
    (charList.exists(_.isUpper), charList.exists(_.isLower)) match {
      case (true, true) => text.valid[List[String]]
      case (_, _) => List("Has a password at least one upper case and one lower case character").invalid[String]
    }
  }
}
