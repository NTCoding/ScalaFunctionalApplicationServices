package infrastructure

import infrastructure.ImplicitLifting._
import scala.concurrent.Future

/*
  This is the intepreter for the application. Takes events and carries out the desired effects
  This is a simplified version of the domain events pattern. It is recommended to use a 
  more structured and typesafe approach rather than AnyRef
  See Patterns, Principles and Practices of Domain-Driven Design for full exmaples
*/
object EventProcessor {
	type Event = AnyRef
	type InterpretEvents = Seq[Event] => Future[Unit]
	
	def apply(es: Seq[Event]): Future[Unit] =
		es.foreach { e => println("Processing event: $e") }
}