package bitrock.command

sealed trait Command

object Command {
  final case class AddPlayer(name: String) extends Command

  final case object Start extends Command

  final case class MovePlayer(name: String, firstDice: Int, secondDice: Int) extends Command
}
