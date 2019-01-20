package bitrock.logic

final case class GameState(
  currentPlayer: Option[String] = None,
  players: Seq[String] = Seq.empty,
  playersPositions: Map[String, Int] = Map.empty,
  allowNewPlayers: Boolean = true,
  playersOnFinishLine: Seq[String] = Seq.empty,
  isFinished: Boolean = false
)

object GameState {
  def empty: GameState = GameState()
}
