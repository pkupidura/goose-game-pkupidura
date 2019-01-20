package bitrock

sealed trait ParseError {
  def show(): String
}

object ParseError {
  final case class UnknownCommand(line: String) extends ParseError {
    def show(): String = s"Unknown command: $line"
  }
}
