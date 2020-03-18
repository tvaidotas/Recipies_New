package controllers

import authentication.AuthenticationAction
import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._

@Singleton
class HomeController @Inject()(cc: ControllerComponents, authAction: AuthenticationAction) extends AbstractController(cc) with I18nSupport{

  def index: Action[AnyContent] = authAction { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

}
