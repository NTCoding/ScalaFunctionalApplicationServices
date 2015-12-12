package config

import controllers.{Referrals, Assets}
import play.api.routing.Router
import play.api.{BuiltInComponentsFromContext, ApplicationLoader}
import play.api.ApplicationLoader.Context
import router.Routes

class ControllerWiring extends ApplicationLoader {
  def load(context: Context) = {
    new SASComponents(context).application
  }
}

class SASComponents(context: Context) extends BuiltInComponentsFromContext(context) {
  override def router: Router = new Routes(httpErrorHandler, referralsController, assets)

  lazy val referralsController = new Referrals(config.Wiring.recommendAFriend, config.Wiring.interpretEvents)
  lazy val assets = new Assets(httpErrorHandler)
}