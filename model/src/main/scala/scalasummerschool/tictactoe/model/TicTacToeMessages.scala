package scalasummerschool.tictactoe.model

import scalasummerschool.tictactoe.model.GameEntities.Field

sealed trait TicTacToeMessage

object TicTacToeMessage {

  case class MoveRequest(username: String, token: String, field: Field) extends TicTacToeMessage
  case class GameResponse(field: Field) extends TicTacToeMessage
}
