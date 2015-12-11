package infrastructure

import scala.concurrent.Future

object ImplicitLifting {
	implicit def toFut[A](a: A): Future[A] = Future.successful(a)
	implicit def toOpt[A](a: A): Option[A] = Some(a)
	implicit def toEither[A](a: A): Either[Nothing, A] = Right(a)
}