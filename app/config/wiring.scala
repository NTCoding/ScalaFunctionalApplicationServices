package config

import infrastructure.EventProcessor.InterpretEvents
import useCases._
import useCases.RecommendAFriend.RecommendAFriend
import domain._
import infrastructure.EventProcessor

// Wire up all the use cases by providing them with the required functions
object Wiring {

  // We're passing in *static* functions, but that's ok. Nothing is tightly coupled to them
	val recommendAFriend: RecommendAFriend = useCases.RecommendAFriend(
		ValidateNewAccount.apply, FindUser.apply, CreateFriend.apply, 
		PromoteToGoldStatus.apply
	)

	val interpretEvents: InterpretEvents = EventProcessor.apply
}