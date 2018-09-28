package scalasummerschool.tictactoe.util

import org.specs2.mutable.Specification

object ValidationSpec extends Specification {

  "Validation" should {
    "Username is non-empty" in {
      Validation.checkNonEmpty("username", "123").toEither must beRight("123")
    }
  }
}
