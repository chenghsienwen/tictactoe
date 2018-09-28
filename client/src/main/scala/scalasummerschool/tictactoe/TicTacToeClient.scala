package scalasummerschool.tictactoe

import cats.effect._

import scala.io.StdIn

object TicTacToeClient extends IOApp {

  def clientLoop(): IO[ExitCode] = 
    for {
      in   <- IO(StdIn.readLine("lambda> Exit client with 'q': "))
      code <- {
        if(in == "q") IO.pure(ExitCode.Success) 
        else          clientLoop()
      }
    } yield code

  def run(args: List[String]): IO[ExitCode] = clientLoop()
}
