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

  final case object GameStarted extends StepResult {
    def show(): String = "Game has started!"
  }

  final case class UnknownPlayer(name: String) extends StepResult {
    def show(): String = s"Unknown player $name!"
  }

  final case object GameAlreadyFinished extends StepResult {
    def show(): String = s"The game has already finished!"
  }

  final case object GameNotStarted extends StepResult {
    def show(): String = s"The game was not started - type 'start'!"
  }

  final case class PlayerMoved(
    name: String,
    firstDice: Int,
    secondDice: Int,
    path: Seq[PlayerMoved.MoveKind],
    finalPosition: Int
  ) extends StepResult {
    def show(): String = s"$name rolls $firstDice, $secondDice. ${path.map(_.show(name)).mkString(" ")}"
  }

  object PlayerMoved {
    sealed trait MoveKind {
      val from: Int
      val to: Int

      def show(name: String): String
    }

    final case class RegularMove(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves from $from to $to"
    }

    final case class RegularMoveOnGoose(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves from $from to $to, the Goose."
    }

    final case class RegularMoveViaBridge(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves from $from to The Bridge. $name jumps to $to"
    }

    final case class GooseMoveOnGoose(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves again and goes to $to, the Goose."
    }

    final case class GooseMoveViaBridge(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves again to The Bridge. $name jumps to $to"
    }

    final case class GooseMove(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves again and goes to $to"
    }

    final case class WinningMove(from: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves from $from to $to. $name Wins!"
    }

    final case class Bounce(from: Int, stop: Int, to: Int) extends MoveKind {
      def show(name: String): String = s"$name moves from $from to $stop. $name bounces! $name returns to $to"
    }
  }
}
