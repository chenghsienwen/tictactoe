package scalasummerschool.tictactoe.service

import scalasummerschool.tictactoe.model.User
import cats.effect.IO
import cats.effect.concurrent.Ref
import org.http4s._
import org.http4s.implicits._
import org.specs2.mutable.Specification

object UserServiceSpec extends Specification {

  "UserService" should {
    "get all users" in {
      val request  = Request[IO](Method.GET, Uri.uri("/"))
      val response = getUserService().flatMap(_.orNotFound(request)).unsafeRunSync()

      response.status must beEqualTo(Status.Ok)
    }

    "create user" in {
      val request  = Request[IO](
        Method.PUT, 
        Uri.uri("/").withQueryParam("username", "test").withQueryParam("password", "pW123")
      )
      val response = getUserService().flatMap(_.orNotFound(request)).unsafeRunSync()

      response.status must beEqualTo(Status.Created)
    }

    "get user" in {
      val username = "test"

      val createRequest = Request[IO](
        Method.PUT, 
        Uri.uri("/").withQueryParam("username", username).withQueryParam("password", "pW123")
      )
      val userRequest = Request[IO](Method.GET, Uri.unsafeFromString(s"/$username"))

      val response = (for {
        service <- getUserService()
        _       <- service.orNotFound(createRequest)
        resp    <- service.orNotFound(userRequest)
      } yield resp).unsafeRunSync()

      response.status must beEqualTo(Status.Ok)
    }

    "delete user" in {
      val username = "test"

      val createRequest = Request[IO](
        Method.PUT, 
        Uri.uri("/").withQueryParam("username", username).withQueryParam("password", "pW123")
      )
      val deleteRequest = Request[IO](Method.DELETE, Uri.unsafeFromString(s"/$username"))
      val userRequest   = Request[IO](Method.GET, Uri.unsafeFromString(s"/$username"))

      val response = (for {
        service <- getUserService()
        _       <- service.orNotFound(createRequest)
        _       <- service.orNotFound(deleteRequest)
        resp    <- service.orNotFound(userRequest)
      } yield resp).unsafeRunSync()

      response.status must beEqualTo(Status.NotFound)
    }

    "authenticate user" in {
      val username = "test"
      val password = "pW123"

      val createRequest = Request[IO](
        Method.PUT, 
        Uri.uri("/").withQueryParam("username", username).withQueryParam("password", password)
      )
      val authenticateRequest = Request[IO](
        Method.POST, 
        Uri.unsafeFromString(s"/authenticate").withQueryParam("username", username).withQueryParam("password", password)
      )

      val response = (for {
        service <- getUserService()
        _       <- service.orNotFound(createRequest)
        resp    <- service.orNotFound(authenticateRequest)
      } yield resp).unsafeRunSync()

      response.status must beEqualTo(Status.Ok)
      response.as[String].unsafeRunSync().length must beEqualTo(32)
    }
  }

  private def getUserService() = {
    val usersIO = Ref.of[IO, Map[String, User]](Map.empty)

    for {
      usersRef <- usersIO
    } yield UserService.build(usersRef)
  }
}
