package scalasummerschool.tictactoe.service

import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object TicTacToeService extends Http4sDsl[IO] {

  def build(): HttpRoutes[IO] = {
    HttpRoutes.of[IO] {
      // Calculate a move
      case req@POST -> Root =>
        ???
    }
  }
}
