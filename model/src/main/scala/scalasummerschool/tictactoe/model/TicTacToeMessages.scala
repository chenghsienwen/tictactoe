package scalasummerschool.tictactoe.model

import cats.effect.IO
import scalasummerschool.tictactoe.model.GameEntities.Field
import io.circe.generic.semiauto._
import org.http4s.circe._

sealed trait TicTacToeMessage

object TicTacToeMessage {
  case class MoveRequest(username: String, token: String, field: Field) extends TicTacToeMessage
  case class GameResponse(field: Field) extends TicTacToeMessage
  implicit val MoveRequestDec = deriveDecoder[MoveRequest]
  implicit val MoveRequestEn  = deriveEncoder[MoveRequest]
  implicit val MoveRequestJson = jsonOf[IO, MoveRequest]
  implicit val GameResponseDec = deriveDecoder[GameResponse]
  implicit val GameResponseEn  = deriveEncoder[GameResponse]
  implicit val GameResponseJson = jsonOf[IO, GameResponse]
}
