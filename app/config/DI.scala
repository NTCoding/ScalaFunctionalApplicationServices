package config 

import com.google.inject.AbstractModule
import infrastructure.EventProcessor.InterpretEvents
import net.codingwell.scalaguice.ScalaModule

class DI extends AbstractModule with ScalaModule {
	override def configure() {
		bind[useCases.RecommendAFriend.RecommendAFriend].toInstance(Wiring.recommendAFriend)
		bind[InterpretEvents].toInstance(Wiring.interpretEvents)
	}
}