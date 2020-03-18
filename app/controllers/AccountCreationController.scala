package controllers

import javax.inject.Inject
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Request}
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.play.json._
import collection._
import models.{LoginDetails, Person}
import models.PersonJsonFormats._
import play.api.libs.json.{JsValue, Json}
import reactivemongo.api.Cursor
import play.modules.reactivemongo.{MongoController, ReactiveMongoApi, ReactiveMongoComponents}

class AccountCreationController @Inject()(
                                                    components: ControllerComponents,
                                                    val reactiveMongoApi: ReactiveMongoApi
                                                  ) extends AbstractController(components)
  with MongoController with ReactiveMongoComponents with play.api.i18n.I18nSupport {

  implicit def ec: ExecutionContext = components.executionContext

  def collection: Future[JSONCollection] = database.map(_.collection[JSONCollection]("persons"))

  def create: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
    Person.accountCreation.bindFromRequest.fold({ formWithErrors =>
      Future.successful(BadRequest(views.html.signup(formWithErrors)))
    }, { person =>
      collection.flatMap(_.insert.one(person)).map { _ => Ok("User inserted")
      }
    })
  }

  //TODO : Improve
//  def delete: Action[AnyContent] = Action.async { implicit request: Request[AnyContent] =>
//    Person.accountCreation.bindFromRequest.fold({ formWithErrors =>
//      Future.successful(BadRequest(views.html.signup(formWithErrors)))
//    }, { person =>
//      collection.flatMap(_.findAndRemove(person)).map { _ => Ok("User inserted")
//      }
//    })
//  }

  def findByUsername(username: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Person]] = collection.map {
      _.find(Json.obj("username" -> username)).
        sort(Json.obj("created" -> -1)).
        cursor[Person]()
    }

    val futureUsersList: Future[List[Person]] =
      cursor.flatMap(
        _.collect[List](
          -1,
          Cursor.FailOnError[List[Person]]()
        )
      )

    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }



  //def Update

  def findByName(name: String): Action[AnyContent] = Action.async {
    val cursor: Future[Cursor[Person]] = collection.map {
      _.find(Json.obj("name" -> name)).
        sort(Json.obj("created" -> -1)).
        cursor[Person]()
    }

    val futureUsersList: Future[List[Person]] =
      cursor.flatMap(
        _.collect[List](
          -1,
          Cursor.FailOnError[List[Person]]()
        )
      )

    futureUsersList.map { persons =>
      Ok(persons.toString)
    }
  }


  def signup() =Action { implicit request: Request[AnyContent] =>
    Ok(views.html.signup(Person.accountCreation))

  }

}
