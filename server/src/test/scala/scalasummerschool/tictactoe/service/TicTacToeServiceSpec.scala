package scalasummerschool.tictactoe.service

import cats.effect.IO
import cats.effect.concurrent.Ref
import io.circe.syntax._
import org.http4s._
import org.http4s.circe._
import org.http4s.implicits._
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.specs2.mutable.Specification
import scalasummerschool.tictactoe.model.GameEntities.GameSymbol.O
import scalasummerschool.tictactoe.model.GameEntities.{GameSymbol, Move}
import scalasummerschool.tictactoe.model.TicTacToeMessage.{GameResponse, MoveRequest}
import scalasummerschool.tictactoe.model.{GameEntities, User}
import scalasummerschool.tictactoe.util.GameLogicUtil

//sbt "testOnly scalasummerschool.tictactoe.service.TicTacToeServiceSpec"
object TicTacToeServiceSpec extends Specification with MockitoSugar{
  private[this] val gameUtil = new GameLogicUtil()
  val mockGameLogicUtil = mock[GameLogicUtil]

  "UserService" should {
    "play game and get movement" in {
      val move1 = Move(O, 0, 0)
      val field1 = GameEntities.doMove(Map.empty[Int, Map[Int, GameSymbol]], move1)
      val move2 = gameUtil.getMove(field1)
      when(mockGameLogicUtil.getMove(field1)).thenReturn(move2)
      val requestBody = MoveRequest("testuser", "testtoken", field1).asJson
      val request = Request[IO](Method.POST, Uri.uri("/")).withEntity(requestBody)
      val response = getGameService(mockGameLogicUtil).flatMap(_.orNotFound(request)).unsafeRunSync()

      val expectResult = GameResponse(move2.map(i => GameEntities.doMove(field1, i)).getOrElse(field1))

      response.status must beEqualTo(Status.Ok)
      response.as[GameResponse].unsafeRunSync() must beEqualTo(expectResult)
    }
  }

  private def getUserService() = {
    val usersIO = Ref.of[IO, Map[String, User]](Map.empty)

    for {
      usersRef <- usersIO
    } yield UserService.build(usersRef)
  }

  private def getGameService(gameLogicUtil: GameLogicUtil) = {
    IO.pure(TicTacToeService.build(gameLogicUtil))
  }
}
