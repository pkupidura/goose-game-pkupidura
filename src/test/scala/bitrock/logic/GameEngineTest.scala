package bitrock.logic

import bitrock.command.Command.AddPlayer
import bitrock.logic.StepResult.{AddingPlayersLocked, DuplicatedPlayer, PlayerAdded}
import org.scalatest.{FlatSpec, Matchers}

class GameEngineTest extends FlatSpec with Matchers {
  behavior.of("GameEngine")

  it should "return updated list of players if added a player that's not yet registered" in {
    val engine = new GameEngine(BoardConfiguration.default, GameState.empty)

    val (_, result) = engine.step(AddPlayer("John"))

    result shouldEqual PlayerAdded(Seq("John"))
  }

  it should "reject adding a player if there is already a player with given name" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 0))
    )

    val (_, result) = engine.step(AddPlayer("John"))

    result shouldEqual DuplicatedPlayer("John")
  }

  it should "reject adding a player if game already started and is not accepting new players" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(allowNewPlayers = false)
    )

    val (_, result) = engine.step(AddPlayer("John"))

    result shouldEqual AddingPlayersLocked
  }
}
