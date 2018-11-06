package http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.directives.MethodDirectives
import dao.DogDao
import model.Dog

import scala.concurrent.ExecutionContextExecutor

trait DogRoutes extends SprayJsonSupport {
  this: DogDao =>

  implicit val dispatcher: ExecutionContextExecutor

  val routes = pathPrefix("dogs") {
    pathEnd {
      get {
        complete(getAll)
      } ~ post {
        entity(as[Dog]) { dog =>
          complete {
            create(dog).map { result => HttpResponse(entity = "dog has been saved successfully") }
          }
        }
      }
    } ~
      path(IntNumber) { id =>
        get {
          complete(getById(id))
        } ~ put {
          entity(as[Dog]) { dog =>
            complete {
              val newDog = Dog(dog.name, Option(id))
              update(newDog).map { result => HttpResponse(entity = "dog has been updated successfully") }
            }
          }
        } ~ MethodDirectives.delete {
          complete {
            delete(id).map { result => HttpResponse(entity = "dog has been deleted successfully") }
          }
        }
      }
  }
}