package bitrock.logic

sealed trait StepResult {
  def show(): String
}

object StepResult {
  final case class PlayerAdded(players: Seq[String]) extends StepResult {
    def show(): String = s"players: ${players.mkString(", ")}"
  }

  final case class DuplicatedPlayer(name: String) extends StepResult {
    def show(): String = s"$name: already existing player!"
  }

  final case object AddingPlayersLocked extends StepResult {
    def show(): String = "Cannot add players after game already started!"
  }
}
