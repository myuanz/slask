package example


case class MainView() {
  var view = new Blueprints
  view.+=(Router("GET", "/", context => {
    new HTMLResponse("test title", context.header.mkString("<br/>"))
  }))
}
