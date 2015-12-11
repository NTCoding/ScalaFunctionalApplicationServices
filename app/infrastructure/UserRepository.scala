package infrastructure

import domain._
import domain.Referrals._
import useCases._

/*
	Still ok to have objects with multiple functions. Just don't depend
	directly on them, therefore they can be static
*/
object UserRepository {
	type Email = String
	type Nickname = String
	type Age = Int
	
	def findById(i: UserId): Option[User] = Some(User(i))
	def create(e: Email, n: Nickname, a: Age): User = null
}