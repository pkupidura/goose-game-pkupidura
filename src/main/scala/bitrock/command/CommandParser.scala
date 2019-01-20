package bitrock.command

object CommandParser {

  def parse(line: String): Either[ParseError, Command] =
    line.split(' ').toList match {
      case "add" :: "player" :: name :: Nil =>
        Right(Command.AddPlayer(name.trim))

      case _ =>
        Left(ParseError.UnknownCommand(line))
    }
}
