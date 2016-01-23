import LOLFutureMocks._
import LOLMocks._
import domain.{PromoteToGoldStatus, User}
import org.scalatest.{FreeSpec, Matchers}
import play.api.test.{DefaultAwaitTimeout, FutureAwaits}
import useCases.{NewAccountDetails, RecommendAFriend}

class TestWithoutMocks extends FreeSpec with Matchers with FutureAwaits with DefaultAwaitTimeout {
  val userId = "12345"
  val user = User(userId)
  val friend = User("friendId")
  val friendsDetails = NewAccountDetails("abc@mailinator.com", "abb", 1)
  val results = Seq(PromoteToGoldStatus(userId))

  "When an existing user" - {
    "recommends a friend using details that validate" - {
      "the friend is created and the referral policy's decision is returned" in {
        val f = RecommendAFriend(
          ret(friendsDetails)(None), retF(userId)(Some(user)), retF(friendsDetails)(friend), ret(user, friend)(results)
        ) _
        assert(await(f(userId, friendsDetails)) === Right(results))
      }
    }
  }
}

class SetExample extends FreeSpec with Matchers {
  val userEmail = "abc@mailinator.com"
  var sentEmailTo = "NOTSENT"

  "When the winner is selected they are immediately notified by email" in {
    SelectWinnerOfCompetition(ret(userEmail), set(sentEmailTo = _))()
    assert(sentEmailTo === userEmail)
  }

  def ret[A](a: A): Unit => A = _ => a

  // TODO - carry out some side effect i.e 'set' a value indicating the side effect occurred
  def set[A](f: A => Unit): A => Unit = f(_)
}

object SelectWinnerOfCompetition {
  type EmailAddress = String
  type SelectWinner = Unit => EmailAddress
  type SendWinnerEmail = EmailAddress => Unit

  def apply(s: SelectWinner, e: SendWinnerEmail)() = e(s())
}
