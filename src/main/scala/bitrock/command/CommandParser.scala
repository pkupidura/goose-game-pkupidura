package bitrock.command

import scala.util.control.Exception._

object CommandParser {

  def parse(line: String): Either[ParseError, Command] =
    line.split(' ').toList match {
      case "add" :: "player" :: name :: Nil =>
        Right(Command.AddPlayer(name.trim))

      case "start" :: Nil =>
        Right(Command.Start)

      case "move" :: name :: firstDiceWithComa :: secondDice :: Nil =>
        val firstDiceOpt = parseDiceValue(firstDiceWithComa.stripSuffix(","))
        val secondDiceOpt = parseDiceValue(secondDice)

        (firstDiceOpt, secondDiceOpt) match {
          case (Some(first), Some(second)) =>
            Right(Command.MovePlayer(name, first, second))

          case _ =>
            Left(ParseError.IncorrectDiceValue(line))
        }

      case "move" :: name :: Nil =>
        Right(Command.MovePlayerRandomly(name))

      case _ =>
        Left(ParseError.UnknownCommand(line))
    }

  private def parseDiceValue(str: String): Option[Int] =
    parseInt(str).filter(d => d >= 1 && d <= 6)

  private def parseInt(str: String): Option[Int] =
    allCatch.opt(str.toInt)
}
