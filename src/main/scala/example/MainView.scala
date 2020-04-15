package example;
import fun.myuan.slask._

case class MainView() {
  var view = new Blueprints
  view.+=(Router("GET", "/", context => new HTMLResponse("myuan.fun", context.header.mkString("<br/>"))))
  view.+=(Router("GET", "/favicon.ico", context => new StaticResponse("favicon.ico")))

}
