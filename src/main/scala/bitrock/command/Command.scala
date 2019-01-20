package bitrock.command

sealed trait Command

object Command {
  final case class AddPlayer(name: String) extends Command
}
