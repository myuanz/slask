package example


import scala.util.matching.Regex


case class Router(method: String, path: String, responseFunction: (Context) => Response) {}

class Blueprints {
  var routers: Array[Router] = new Array[Router](0)

  def matchPath(startLine: String): Array[(Context) => Response] = {
    val reg: Regex = """([A-Z]+) (/.*) HTTP/\d\.\d$""".r
    val method :: path :: _ = reg.findAllMatchIn(startLine).next.subgroups
    routers.filter(router => {
      method == router.method && path == router.path
    }).map(_.responseFunction)
  }

  def +=(router: Router): Unit = {
    routers = routers :+ router
  }
}