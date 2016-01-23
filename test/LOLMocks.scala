import scala.concurrent.Future

object LOLMocks {
  // return the stubbed value if the actual arguments match the expected arguments
  def ret[A,B](a: A)(b: B): A => B =
    x => if (x == a) b else throw ReturnFailed(s"$x did not equal $a")

  def ret[A,B,C](a: A, b: B)(c: C): (A,B) => C =
    (x, y) => if (x == a && y == b) c else throw ReturnFailed(s"$x and $y did not equal $a and $b")
}

object LOLOptionMocks {
  def none[A,B](): A => Option[B] = x => None
}

object LOLFutureMocks {
  def retF[A,B](a: A)(b: B): A => Future[B] =
    x => if (x == a) Future.successful(b) else throw ReturnFailed(s"$x did not equal $a")
}


case class ReturnFailed(msg: String) extends Exception(msg)
