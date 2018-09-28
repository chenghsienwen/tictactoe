package scalasummerschool.tictactoe.model

import scalasummerschool.tictactoe.model.GameEntities.Field

object TicTacToeMessages {
  final case class MoveRequest(username: String, token: String, field: Field)
  final case class GameResponse(field: Field)
}
