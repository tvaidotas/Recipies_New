package models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.OFormat


case class Person(name: String, age: Int, username: String, password: String) {
  val loginDetails: LoginDetails = LoginDetails(username,password)
  override def toString: _root_.java.lang.String = {
    s"Full Name: $name Age: $age Username: $username"
  }
}

object Search{

  val accountSearchUsername: Form[Search] =Form(
    mapping(
      "username" -> nonEmptyText
    )(Search.apply)(Search.unapply)
  )

}

object Person{

  val accountCreation: Form[Person] = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> number(min =0 , max=120),
      "username" -> nonEmptyText,
      "password"-> nonEmptyText

    )(Person.apply)(Person.unapply)
  )


  //  def checkIfUserIsValid(userDetails: LoginDetails) = userList.contains(userDetails)
  //
  //  def getUsername(username: String) = userList.filter(user => user.username == username).headOption
}
object JsonFormats {

  import play.api.libs.json.Json

  implicit val personFormat: OFormat[Person] = Json.format[Person]
  implicit val loginDetailsFormat: OFormat[LoginDetails] = Json.format[LoginDetails]
}

case class Search(username: String)


