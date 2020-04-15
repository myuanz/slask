package fun.myuan.slask


import scala.util.matching.Regex


case class Router(method: String, path: String, responseFunction: (Context) => Response) {}

class Blueprints {
  var routers: Array[Router] = new Array[Router](0)

  def notFoundResponseFunction(context: Context): Response = new ExceptionResponse(NotFound())
  def methodNotAllowedResponseFunction(context: Context): Response = new ExceptionResponse(MethodNotAllowed())

  def matchPath(startLine: String): Array[(Context) => Response] = {
    val reg: Regex = """([A-Z]+) (/.*) HTTP/\d\.\d$""".r
    val method :: path :: _ = reg.findAllMatchIn(startLine).next.subgroups
    // println(s"method: ${method}, path: ${path}", routers.length)
    routers.filter(x=>x.method==method && x.path==path).map(_.responseFunction)
  }

  def +=(router: Router): Unit = {
    routers = routers :+ router
  }
}