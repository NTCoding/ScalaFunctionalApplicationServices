package controllers

import infrastructure.EventProcessor.InterpretEvents
import infrastructure.ImplicitLifting._
import play.api._
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Results._
import useCases.RecommendAFriend.RecommendAFriend
import useCases._

class Referrals (recommendAFriend: RecommendAFriend, interpret: InterpretEvents) {

	def get = Action { 
		Ok(views.html.recommendAFriend(referralForm))
	}

	def post = Action.async { implicit request =>
		referralForm.bindFromRequest.fold(
			errors => BadRequest(views.html.recommendAFriend(errors)),
			model => recommendAFriend(model.userId, model.newAccount) flatMap {
				case Left(errors) => 
					Logger.error(s"Error recommending a friend: $errors")
					InternalServerError(views.html.recommendAFriendError())
				case Right(events) => interpret(events) map { _ => Ok(views.html.recommendThanks()) }
			}
		)
	}

	lazy val referralForm = Form(mapping(
		"userId" -> nonEmptyText,
		"newAccount" -> newAccountMapping
	)(ReferralForm.apply)(ReferralForm.unapply))

	lazy val newAccountMapping = mapping(
		"email" -> nonEmptyText,
		"nickname" -> nonEmptyText,
		"age" -> number(min = 18, max = 200)
	)(NewAccountDetails.apply)(NewAccountDetails.unapply)
}

case class ReferralForm(userId: String, newAccount: NewAccountDetails)
