package bitrock

object CommandParser {

  def parse(line: String): Either[ParseError, Command] =
    line.split(' ').toList match {
      case "add" :: "player" :: name :: Nil =>
        Right(Command.AddPlayer(name))

      case _ =>
        Left(ParseError.UnknownCommand(line))
    }
}
