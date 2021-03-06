package bitrock.logic

final case class BoardConfiguration(
  length: Int,
  bridges: Map[Int, Int],
  gooses: Set[Int]
)

object BoardConfiguration {
  def default: BoardConfiguration = BoardConfiguration(63, Map(6 -> 12), Set(5, 9, 14, 18, 23, 27))
}
