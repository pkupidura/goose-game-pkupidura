package bitrock.logic

final case class GameState(
  players: Seq[String] = Seq.empty,
  playersPositions: Map[String, Int] = Map.empty,
  inProgress: Boolean = false,
  isFinished: Boolean = false
)

object GameState {
  def empty: GameState = GameState()
}
