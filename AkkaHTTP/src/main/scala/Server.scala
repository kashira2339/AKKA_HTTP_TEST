import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import dao.DogDao
import model.Dog
import http.DogRoutes
import scala.io.StdIn

import scala.concurrent.ExecutionContextExecutor

object Server extends App with DogRoutes with DogDao {

  implicit val system: ActorSystem = ActorSystem()

  implicit val materializer = ActorMaterializer()

  implicit val dispatcher: ExecutionContextExecutor = system.dispatcher

  ddl.onComplete {
    _ =>
      create(Dog("Bailey"))
      create(Dog("Max"))
      create(Dog("Charlie"))
      create(Dog("Bella"))
      create(Dog("Lucy"))
      create(Dog("Molly"))
      val bindingFuture = Http().bindAndHandle(routes, "localhost", 8080)
      println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
      StdIn.readLine()
      bindingFuture
        .flatMap(_.unbind())
        .onComplete(_ => system.terminate())
  }
}