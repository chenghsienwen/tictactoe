package scalasummerschool.tictactoe.util

import scalasummerschool.tictactoe.model.GameEntities._

import scala.util.Random

class GameLogicUtil {
  private val rand = new Random()

  /**
    * The "AI" will compute apply a `Move` to a given `Field`, if that is still possible.
    *
    * Optional TODO: Improve the "AI". It's terribly dull at the moment.
    */

  def getMove(field: Field): Option[Move] = {
    if (!isFull(field)) {
      val emptyCells = for {
        rowIndex <- 0 to 2
        colIndex <- 0 to 2
        if !isSet(field, rowIndex, colIndex)
      } yield Move(GameSymbol.O, rowIndex, colIndex)

      Some(rand.shuffle(emptyCells.toList).head)
    } else {
      None
    }
  }
}
