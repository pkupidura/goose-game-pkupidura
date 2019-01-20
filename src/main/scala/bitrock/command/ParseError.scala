package bitrock.command

sealed trait ParseError {
  def show(): String
}

object ParseError {
  final case class IncorrectDiceValue(line: String) extends ParseError {
    def show(): String = s"Provided value of dices were incorrect: $line"
  }

  final case class UnknownCommand(line: String) extends ParseError {
    def show(): String = s"Unknown command: $line"
  }
}
