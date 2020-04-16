package fun.myuan.slask


case class Router(method: String, path: String, responseFunction: (Context) => Response) {}

class Blueprints {
  var routers: Array[Router] = new Array[Router](0)

  def notFoundResponseFunction(context: Context): Response = new ExceptionResponse(NotFound())

  def methodNotAllowedResponseFunction(context: Context): Response = new ExceptionResponse(MethodNotAllowed())

  def matchPath(startLine: String): Array[(Context) => Response] = {
    val url: URL = new URL(startLine)
    routers.filter(x => x.method == url.method && x.path == url.path.split("[?]")(0)).map(_.responseFunction)
  }

  def +=(router: Router): Unit = {
    routers = routers :+ router
  }
}