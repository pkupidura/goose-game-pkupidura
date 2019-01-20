package bitrock

import bitrock.Command.AddPlayer
import StepResult._

class GameEngine private (config: BoardConfiguration, state: GameState) {

  def isFinished: Boolean = state.isFinished

  def currentPlayer: Option[String] = state.currentPlayer

  def step(command: Command): (GameEngine, StepResult) =
    command match {
      case AddPlayer(name) => withNewPlayer(name)
    }

  private def withNewPlayer(name: String): (GameEngine, StepResult) =
    if (!state.allowNewPlayers) {
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

  private def withoutStateUpdate(result: StepResult) = (this, result)

  private def withUpdatedState(result: StepResult)(patch: GameState => GameState) =
    (new GameEngine(config, patch(state)), result)
}

object GameEngine {
  def apply(config: BoardConfiguration): GameEngine = new GameEngine(config, GameState.empty)
}
