import domain.{PromoteToGoldStatus, ReferralOutcome, User}
import org.scalatest.mock.MockitoSugar
import org.scalatest.{FreeSpec, Matchers}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import useCases.RecommendAFriend.RecommendAFriendResult
import useCases.{NewAccountDetails, RecommendAFriendError}
import org.mockito.Mockito._
import org.mockito.stubbing._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class RecommendAFriendSpecs extends FreeSpec with Matchers with FutureAwaits with DefaultAwaitTimeout {
  val repo = mock(classOf[UserRepository])
  val validator = mock(classOf[NewAccountValidator])
  val policy = mock(classOf[ReferralPolicy])

  val userId = "12345"
  val user = User(userId)
  val friend = User("friendId")
  val friendsDetails = NewAccountDetails("abc@mailinator.com", "abb", 1)
  val results = Seq(PromoteToGoldStatus(userId))

  "When an existing user" - {
    when(repo.find(userId)).thenReturn(Future.successful(Some(user)))

    "recommends a friend using details that validate" - {
      when(validator.validate(friendsDetails)).thenReturn(None)

      "the friend is created and the referral policy's decision is returned" in {
        when(repo.create(friendsDetails)).thenReturn(Future.successful(friend))
        when(policy.apply(user, friend)).thenReturn(results)

        val f = new FriendRecommender(validator, repo, policy).recommendAFriend(userId, friendsDetails)
        assert(await(f) === Right(results))
      }
    }
  }
}

class FriendRecommender(validator: NewAccountValidator, repo: UserRepository, policy: ReferralPolicy) {
  def recommendAFriend(userId: String, friendsDetails: NewAccountDetails): Future[RecommendAFriendResult] =
    validator.validate(friendsDetails) match {
      case None =>
        for (
          friend <- repo.create(friendsDetails);
          user <- repo.find(userId)
        ) yield { Right(policy.apply(user.get, friend)) }
      case Some(error) => Future.successful(Left(error))
    }
}

trait NewAccountValidator {
  def validate(newAccount: NewAccountDetails): Option[RecommendAFriendError]
}

trait UserRepository {
  def find(id: String): Future[Option[User]]
  def create(details: NewAccountDetails): Future[User]
}

trait ReferralPolicy {
  def apply(user: User, friend: User): Seq[ReferralOutcome]
}

