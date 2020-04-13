package example


import scala.util.matching.Regex


case class Router(method: String, path: String, responseFunction: (Context) => Response) {}

class Blueprints {
  var routers: Array[Router] = new Array[Router](0)

  def notFoundResponseFunction(context: Context): Response = new ExceptionResponse(NotFound())
  def methodNotAllowedResponseFunction(context: Context): Response = new ExceptionResponse(MethodNotAllowed())

  def matchPath(startLine: String): Array[(Context) => Response] = {
    val reg: Regex = """([A-Z]+) (/.*) HTTP/\d\.\d$""".r
    val method :: path :: _ = reg.findAllMatchIn(startLine).next.subgroups

    for (route <- routers) yield {
      route match {
        case r: Router if method == r.method && path == r.path => route.responseFunction
        case r: Router if path == r.path => this.methodNotAllowedResponseFunction _
        case _ => this.notFoundResponseFunction _
      }
    }
  }

  def +=(router: Router): Unit = {
    routers = routers :+ router
  }
}