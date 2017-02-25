import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.Config
import entities.LostObject

import scala.concurrent.ExecutionContextExecutor

trait Service extends Protocols {

  implicit val system: ActorSystem
  implicit val executor: ExecutionContextExecutor
  implicit val materializer: ActorMaterializer

  def config: Config
  val logger: LoggingAdapter

  var lostObjects = Vector.empty[LostObject]


  def saveObject(lostObject: LostObject) = lostObjects :+ lostObject

  val routes: Route = {
    pathPrefix("lost-object") {
      pathEnd {

        path("") {
          get {
            complete(HttpEntity(""))
          }
        }
      }
    }
  }

}
