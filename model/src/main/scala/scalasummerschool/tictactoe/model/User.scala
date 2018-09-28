package scalasummerschool.tictactoe.model

import cats.effect.IO
import io.circe._
import io.circe.generic.semiauto._
import org.http4s.circe._

final case class User(name: String, password: String)

object User {

  implicit val userDec = deriveDecoder[User]
  implicit val userEn  = deriveEncoder[User]

  implicit val userJson = jsonOf[IO, User]
}
