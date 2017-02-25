object entities {
  case class LostObject(id: Long, title: Option[String], description: Option[String], place: Option[String])
}
