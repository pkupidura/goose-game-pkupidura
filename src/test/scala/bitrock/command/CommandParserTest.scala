package bitrock.command

import bitrock.command.ParseError.UnknownCommand
import org.scalatest.{FlatSpec, Matchers}

class CommandParserTest extends FlatSpec with Matchers {
  import Command._

  behavior.of("CommandParser.parse")

  it should "recognize command for adding new player" in {
    CommandParser.parse("add player John") shouldEqual Right(AddPlayer("John"))
  }

  it should "fail for unknown command" in {
    CommandParser.parse("new player") shouldEqual Left(UnknownCommand("new player"))
  }
}
