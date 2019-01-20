package bitrock.logic

import bitrock.command.Command.{AddPlayer, MovePlayer, MovePlayerRandomly}
import bitrock.logic.StepResult._
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
      GameState(inProgress = true)
    )

    val (_, result) = engine.step(AddPlayer("John"))

    result shouldEqual AddingPlayersLocked
  }

  it should "reject player move if game has not started yet" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 0))
    )

    val (_, result) = engine.step(MovePlayerRandomly("John"))

    result shouldEqual GameNotStarted
  }

  it should "move player by given amount of fields" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 0), inProgress = true)
    )

    val (_, result) = engine.step(MovePlayer("John", 1, 1))

    result should matchPattern { case PlayerMoved(_, _, _, _, 2) => }
  }

  it should "move player via bridge if he happens to step on it" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 4), inProgress = true)
    )

    val (_, result) = engine.step(MovePlayer("John", 1, 1))

    result should matchPattern { case PlayerMoved(_, _, _, _, 12) => }
  }

  it should "make a single jump via goose" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 12), inProgress = true)
    )

    val (_, result) = engine.step(MovePlayer("John", 1, 1))

    result should matchPattern { case PlayerMoved(_, _, _, _, 16) => }
  }

  it should "make a double jump via goose" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 10), inProgress = true)
    )

    val (_, result) = engine.step(MovePlayer("John", 2, 2))

    result should matchPattern { case PlayerMoved(_, _, _, _, 22) => }
  }

  it should "bounce if overjumped winning field" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 61), inProgress = true)
    )

    val (_, result) = engine.step(MovePlayer("John", 2, 5))

    result should matchPattern { case PlayerMoved(_, _, _, _, 58) => }
  }

  it should "finish the game if a player steps on a winning field" in {
    val engine = new GameEngine(
      BoardConfiguration.default,
      GameState(players = Seq("John"), playersPositions = Map("John" -> 61), inProgress = true)
    )

    val (game, result) = engine.step(MovePlayer("John", 1, 1))

    result should matchPattern { case PlayerMoved(_, _, _, _, 63) => }
    assert(game.isFinished)
  }
}
