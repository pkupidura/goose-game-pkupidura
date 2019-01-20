package bitrock.command

import bitrock.command.ParseError.{IncorrectDiceValue, UnknownCommand}
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

  it should "recognize command for moving player" in {
    CommandParser.parse("move John 4, 5") shouldEqual Right(MovePlayer("John", 4, 5))
  }

  it should "fail if dice value is not a number" in {
    val line = "move John a, 4"
    CommandParser.parse(line) shouldEqual Left(IncorrectDiceValue(line))
  }

  it should "fail if dice value is higher than 6" in {
    val line = "move John 12, 4"
    CommandParser.parse(line) shouldEqual Left(IncorrectDiceValue(line))
  }

  it should "fail if dice value is lower than 1" in {
    val line = "move John 0, 4"
    CommandParser.parse(line) shouldEqual Left(IncorrectDiceValue(line))
  }
}
