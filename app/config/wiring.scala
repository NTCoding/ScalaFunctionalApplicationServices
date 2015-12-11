package config

import useCases._
import domain._
import infrastructure.EventProcessor
import infrastructure.EventProcessor._

// Wire up all the use cases by providing them with the required functions
object Wiring {

  // We're passing in *static* functions, but that's ok. Nothing is tightly coupled to them
	val recommendAFriend: RecommendAFriend.RecommendAFriend = RecommendAFriend(
		ValidateNewAccount.apply, FindUser.apply, CreateFriend.apply, 
		PromoteToGoldStatus.apply
	)

	val interpretEvents: InterpretEvents = EventProcessor.apply
}