package useCases

import domain._
import domain.Referrals._
import infrastructure.UserRepository
import infrastructure.ImplicitLifting._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RecommendAFriend {
	// This is the application service's signature
	type RecommendAFriendResult = Either[RecommendAFriendError, Seq[ReferralOutcome]]
	type RecommendAFriend = (UserId, NewAccountDetails) => Future[Either[RecommendAFriendError, Seq[ReferralOutcome]]]
	
	type ValidateNewAccount = NewAccountDetails => Option[RecommendAFriendError]
	type FindUser = UserId => Future[Option[User]]
	type CreateFriend = NewAccountDetails => Future[User]
	
  // the first argument list of apply is the constructor
	def apply(v: ValidateNewAccount, f: FindUser, c: CreateFriend, p: ReferralPolicy)
					 (u: UserId, n: NewAccountDetails): Future[RecommendAFriendResult] = 
		v(n) map { err } getOrElse { f(u) flatMap {
			case Some(us) => c(n) map { p(us, _) }
			case None => fer
		}}

	private def err(e: RecommendAFriendError) = Future.successful(Left(e))
	private val fer = Left(RecommendAFriendError("Error finding user"))
}

/*
	Notes
	~~~~~
	We can improve this code in a number of ways. This is just a starting point to show
	how far you can get with a basic understanding of scala. 

	 - optimise readability of Either 
	 - use different Monads for validate so that we focus on success path
	 - clean up the noise of "map" and "flatMap"
*/

case class NewAccountDetails(email: String, nickname: String, age: Int)
case class RecommendAFriendError(friendlyErrorMessage: String)

object ValidateNewAccount {
	def apply(n: NewAccountDetails) = None // pretend we did some validation here
}

object FindUser {
	def apply(u: UserId): Future[Option[User]] = UserRepository.findById(u)
}

object CreateFriend {
	def apply(n: NewAccountDetails): Future[User] = UserRepository.create(n.email, n.nickname, n.age)
}