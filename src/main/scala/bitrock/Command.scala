package bitrock

sealed trait Command

object Command {
  final case class AddPlayer(name: String) extends Command
}
