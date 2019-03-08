package scalasummerschool.tictactoe

import scalasummerschool.tictactoe.service.{TicTacToeService, UserService}
import scalasummerschool.tictactoe.model.User
import cats.effect._
import cats.effect.concurrent.Ref
import cats.implicits._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import scalasummerschool.tictactoe.util.GameLogicUtil

object TicTacToeServer extends IOApp {

  // create a shared Map of user-names to users
  val usersIO = Ref.of[IO, Map[String, User]](Map.empty)
  val gameLogicUtil = new GameLogicUtil()

  def run(args: List[String]): IO[ExitCode] = {
    def runServer(usersRef: Ref[IO, Map[String, User]]) = {
      // compose all services to create one REST api
      val httpApp = Router(
        "/users"     -> UserService.build(usersRef),
        "/tictactoe" -> TicTacToeService.build(gameLogicUtil)
      ).orNotFound

      // build a server application IO
      BlazeServerBuilder[IO]
        .bindHttp(8080, "localhost")
        .withHttpApp(httpApp)
        .serve
        .compile
        .drain
        .as(ExitCode.Success)
    }

    for {
      usersRef <- usersIO
      code     <- runServer(usersRef)
    } yield code
  }
}

