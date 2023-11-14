package binaryTreeScala

trait ElementProcessor[T] {
  def toDo(v: T): Unit
}
