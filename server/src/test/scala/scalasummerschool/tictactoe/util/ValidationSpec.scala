package scalasummerschool.tictactoe.util

import org.specs2.mutable.Specification

object ValidationSpec extends Specification {

  "Validation" should {
    "Username is non-empty" in {
      Validation.checkNonEmpty("username", "123").toEither must beRight("123")
    }

    "The username and password should be at least 3 characters long" in {
      Validation.checkLength("123").toEither must beRight("123")
      Validation.checkLength("12").toEither must beLeft(List("The username and password should be at least 3 characters long"))
    }

    "Has a password at least one upper case and one lower case character" in {
      Validation.checkUpperLowerChar("Aa").toEither must beRight("Aa")
      Validation.checkUpperLowerChar("AA").toEither must beLeft(List("Has a password at least one upper case and one lower case character"))
      Validation.checkUpperLowerChar("aa").toEither must beLeft(List("Has a password at least one upper case and one lower case character"))
    }
  }
}
