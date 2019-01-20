package bitrock
import scala.annotation.tailrec
import scala.io.StdIn

import bitrock.command.CommandParser
import bitrock.logic.{BoardConfiguration, GameEngine}

object GooseGame {

  def main(args: Array[String]): Unit = {
    val config = BoardConfiguration.default
    val game = GameEngine(config)

    loop(game)
  }

  @tailrec
  def loop(game: GameEngine): GameEngine =
    if (game.isFinished) {
      game
    } else {
      val line = StdIn.readLine("> ")

      loop(
        CommandParser.parse(line) match {
          case Left(error) =>
            println(error.show())
            game

          case Right(command) =>
            val (updatedGame, result) = game.step(command)
            println(result.show())
            updatedGame
        }
      )
    }
}
