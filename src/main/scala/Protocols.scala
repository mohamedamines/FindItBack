import entities.LostObject
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait Protocols extends DefaultJsonProtocol {

  implicit val lostObjectFormat = jsonFormat4(LostObject.apply)

}
