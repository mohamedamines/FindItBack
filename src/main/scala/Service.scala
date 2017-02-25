import akka.Done
import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import entities.LostObject

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import scala.concurrent.{ExecutionContextExecutor, Future}

trait Service extends Protocols {

  implicit val system: ActorSystem
  implicit val executor: ExecutionContextExecutor
  implicit val materializer: ActorMaterializer

  def config: Config
  val logger: LoggingAdapter

  // (fake) async database query api
  def saveObject(lostObject: LostObject): Future[Done] = ???
  def fetchObject(itemId: Long): Future[Option[LostObject]] = ???

  val routes: Route = {
    post {
      path("save-object") {
        entity(as[LostObject]) { lostObject =>
          val saved: Future[Done] = saveObject(lostObject)
          onComplete(saved) { _ =>
            complete("saved object")
          }
        }
      }
    } ~
    get {
      pathPrefix("fetch-object" / LongNumber) { id =>
        val maybeObject: Future[Option[LostObject]] = fetchObject(id)

        onSuccess(maybeObject) {
          case Some(lostObject)  => complete(lostObject)
          case None              => complete(StatusCodes.NotFound)
        }
      }
    }
  }
}
