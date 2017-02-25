import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.io.StdIn

object FindItBackService extends App with Service {

  override implicit val system: ActorSystem = ActorSystem()
  override implicit val executor: ExecutionContextExecutor = system.dispatcher
  override implicit val materializer: ActorMaterializer = ActorMaterializer()

  override val config = ConfigFactory.load()

  override val logger: LoggingAdapter = Logging(system, getClass)

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(routes, config.getString("http.interface"), config.getInt("http.port"))

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind)
    .onComplete(_ => system.terminate)
}
