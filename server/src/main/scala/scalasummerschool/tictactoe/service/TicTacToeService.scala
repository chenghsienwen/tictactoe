package scalasummerschool.tictactoe.service

import cats.effect.IO
import cats.implicits._
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import io.circe.syntax._
import scalasummerschool.tictactoe.model.GameEntities
import scalasummerschool.tictactoe.model.TicTacToeMessage._
import scalasummerschool.tictactoe.util.GameLogicUtil

object TicTacToeService extends Http4sDsl[IO] {
  def build(gameLogicUtil: GameLogicUtil): HttpRoutes[IO] =
    HttpRoutes.of[IO] {
      // Calculate a move
      case req @ POST -> Root =>
        (for {
          req <- req.decodeJson[MoveRequest]
        } yield {
          val move = gameLogicUtil.getMove(req.field)
          Ok(GameResponse(move.map(i => GameEntities.doMove(req.field, i)).getOrElse(req.field)).asJson)
        }).flatten
    }

}
