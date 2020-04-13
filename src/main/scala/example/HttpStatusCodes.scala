package example

abstract class HttpStatusCodes{
  val statusCode: Int
  val description: String
}

object OK extends HttpStatusCodes {
  override val statusCode: Int = 200
  override val description: String = "OK"
}
case class NotFound() extends HttpStatusCodes {
  override val statusCode: Int = 404
  override val description: String = "Page Not Found"
}
case class MethodNotAllowed() extends HttpStatusCodes {
  override val statusCode: Int = 405
  override val description: String = "Method Not Allowed"
}
