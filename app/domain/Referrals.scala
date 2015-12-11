package domain 

// This pattern is just to simplify the exmaple
object Referrals {
	type UserId = String
	type Friend = User // purely for explicitness
	type ReferralPolicy = (User, Friend) => Seq[ReferralOutcome]
}

sealed trait ReferralOutcome
case class CreditAccount(userId: String) extends ReferralOutcome
case class PromoteToGoldStatus(userId: String) extends ReferralOutcome

object PromoteToGoldStatus {
	import Referrals.Friend

	def apply(u: User, f: Friend): Seq[ReferralOutcome] = Seq(PromoteToGoldStatus(u.id))
}
