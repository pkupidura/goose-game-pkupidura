package bitrock

final case class GameState(
  currentPlayer: Option[String],
  players: Seq[String],
  playersPositions: Map[String, Int],
  allowNewPlayers: Boolean,
  playersOnFinishLine: Seq[String]
)

object GameState {
  def empty: GameState = GameState(
    currentPlayer = None,
    players = Seq.empty,
    playersPositions = Map.empty,
    allowNewPlayers = true,
    playersOnFinishLine = Seq.empty
  )
}
