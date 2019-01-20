package bitrock

class GameEngine private (config: BoardConfiguration, state: GameState) {
  def step(command: Command): (GameEngine, StepResult) = ???
}

object GameEngine {
  def apply(config: BoardConfiguration): GameEngine = new GameEngine(config, GameState.empty)
}
