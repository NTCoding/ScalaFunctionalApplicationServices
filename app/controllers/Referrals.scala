package controllers

import javax.inject.Inject
import play.api._
import play.api.mvc._
import play.api.mvc.Action
import play.api.mvc.Results._
import play.api.data.Form
import play.api.data.Forms._

import useCases._
import useCases.RecommendAFriend.RecommendAFriend
import infrastructure.ImplicitLifting._
import infrastructure.EventProcessor.InterpretEvents
import config.Wiring

import play.api.libs.concurrent.Execution.Implicits.defaultContext

class Referrals () {
	// TODO - wire up with DI (not so simple using aliases)
	def recommendAFriend = Wiring.recommendAFriend
	def interpret: InterpretEvents = Wiring.interpretEvents
	
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
