package scalasummerschool.tictactoe.service

import java.util.UUID

import cats.effect.IO
import cats.effect.concurrent.Ref
import io.circe.Json
import io.circe.syntax._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, QueryParamDecoder}
import scalasummerschool.tictactoe.model.{Password, User, UserName}

import cats.implicits._

object UserService extends Http4sDsl[IO] {

  object UserNameQueryParamMatcher extends QueryParamDecoderMatcher[String]("username")
  private implicit val userNameQueryParamDecoder: QueryParamDecoder[UserName] = {
    QueryParamDecoder[String].map(UserName)
  }
  object PasswordQueryParamMatcher extends QueryParamDecoderMatcher[String]("password")
  private implicit val passwordQueryParamDecoder: QueryParamDecoder[Password] = {
    QueryParamDecoder[String].map(Password)
  }

  private def putStrLn(value: String): IO[Unit] = IO.delay(println(value))

  def build(usersRef: Ref[IO, Map[String, User]]): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // Get a list of all users
      case GET -> Root =>
          (for {
          ref <-usersRef.get
          //_ <- putStrLn(ref.asJson.noSpaces)
          } yield {
            Ok(Json.obj("users" -> ref.asJson))
          }).flatten
      case PUT -> Root :? UserNameQueryParamMatcher(userName) +& PasswordQueryParamMatcher(password) => {
        val user = User(userName, password)
        (for {
          _ <- usersRef.modify(x => (x + (userName -> user), x))
        } yield {
          Created(user.asJson)
        }).flatten
      }
      case GET -> Root / username => {
        IO.delay{usersRef.get.flatMap { users =>
          users.find(i => i._1 == username) match {
            case Some(r) => Ok(Json.obj("user" -> r._2.asJson))
            case None => NotFound(username)
          }
        }}.flatten
      }
      case DELETE -> Root / username => {
        (for {
          map <- usersRef.get
          _ <- usersRef.getAndSet(map.filterNot(i => i._1 == username))
        } yield {
          Ok(username)
        }).flatten
      }
      case POST -> Root / "authenticate" :? UserNameQueryParamMatcher(userName) +& PasswordQueryParamMatcher(password) => {
        val makeId: IO[String] = IO { UUID.randomUUID().toString }
        (for {
          users <- usersRef.get
          id <- makeId
          //_ <- putStrLn(s"make id: $id")
        } yield {
          users.find{i =>
            val user = i._2
            user.name == userName && user.password == password
          } match {
            case Some(r) => Ok(id)
            case None => NotFound(userName)
          }
        }).flatten
      }
    }
  }
}
