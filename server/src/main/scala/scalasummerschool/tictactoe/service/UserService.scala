package scalasummerschool.tictactoe.service

import scalasummerschool.tictactoe.model.User
import cats.effect.IO
import cats.effect.concurrent.Ref
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

object UserService extends Http4sDsl[IO] {

  def build(usersRef: Ref[IO, Map[String, User]]): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // Get a list of all users
      case GET -> Root =>
        usersRef.get.flatMap { users =>
          Ok(Json.obj("users" -> users.asJson))
        }
    }
  }
}
