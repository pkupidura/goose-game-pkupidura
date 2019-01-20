package bitrock.logic

import scala.util.Random

import bitrock.command.Command
import bitrock.command.Command.AddPlayer
import bitrock.logic.StepResult._

class GameEngine private[logic] (config: BoardConfiguration, state: GameState, random: Random = new Random()) {

  def isFinished: Boolean = state.isFinished

  def step(command: Command): (GameEngine, StepResult) =
    command match {
      case _ if isFinished => withoutStateUpdate(GameAlreadyFinished)

      case AddPlayer(name) => withNewPlayer(name)

      case Command.Start => startedGame()

      case Command.MovePlayer(name, firstDice, secondDice) => withPlayerMoved(name, firstDice, secondDice)

      case Command.MovePlayerRandomly(name) => withPlayerMoved(name, randomDice, randomDice)
    }

  private def withPlayerMoved(name: String, firstDice: Int, secondDice: Int): (GameEngine, StepResult) =
    if (!state.inProgress) {
      withoutStateUpdate(StepResult.GameNotStarted)
    } else {
      state.playersPositions.get(name) match {
        case Some(currentPosition) =>
          val path = movePlayer(currentPosition, firstDice + secondDice)
          val finalPosition = path.last.to

          withUpdatedState(PlayerMoved(name, firstDice, secondDice, path, finalPosition)) { oldState =>
            oldState.copy(
              playersPositions = oldState.playersPositions.updated(name, finalPosition),
              isFinished = isWinningPosition(finalPosition)
            )
          }

        case None =>
          withoutStateUpdate(UnknownPlayer(name))
      }
    }

  private def movePlayer(position: Int, length: Int): Seq[PlayerMoved.MoveKind] = {
    import PlayerMoved._

    val destination = position + length

    if (isWinningPosition(destination)) {
      Seq(WinningMove(position, destination))
    } else if (isBeyondWinningPosition(destination)) {
      val bounceBack = config.length - (destination - config.length)
      Seq(Bounce(position, config.length, bounceBack))
    } else if (config.gooses.contains(position)) {
      config.bridges.get(destination) match {
        case Some(bridgeEnd) => Seq(GooseMoveViaBridge(position, bridgeEnd))
        case None if config.gooses.contains(destination) => GooseMoveOnGoose(position, destination) +: movePlayer(destination, length)
        case _ => Seq(GooseMove(position, destination))
      }
    } else {
      config.bridges.get(destination) match {
        case Some(bridgeEnd) => Seq(RegularMoveViaBridge(position, bridgeEnd))
        case None if config.gooses.contains(destination) => RegularMoveOnGoose(position, destination) +: movePlayer(destination, length)
        case _ => Seq(RegularMove(position, destination))
      }
    }
  }

  private def startedGame(): (GameEngine, StepResult) =
    withUpdatedState(GameStarted)(_.copy(inProgress = true))

  private def withNewPlayer(name: String): (GameEngine, StepResult) =
    if (state.inProgress) {
      withoutStateUpdate(AddingPlayersLocked)
    } else {
      if (state.players.contains(name)) {
        withoutStateUpdate(DuplicatedPlayer(name))
      } else {
        val updatedPlayers = state.players :+ name

        withUpdatedState(PlayerAdded(updatedPlayers)) { oldState =>
          oldState.copy(players = updatedPlayers, playersPositions = state.playersPositions.updated(name, 0))
        }
      }
    }

  private def isBeyondWinningPosition(position: Int): Boolean = position > config.length

  private def isWinningPosition(position: Int): Boolean = position == config.length

  private def randomDice: Int = 1 + random.nextInt(6)

  private def withoutStateUpdate(result: StepResult) = (this, result)

  private def withUpdatedState(result: StepResult)(patch: GameState => GameState) =
    (new GameEngine(config, patch(state)), result)
}

object GameEngine {
  def apply(config: BoardConfiguration): GameEngine = new GameEngine(config, GameState.empty)
}
