package example


import fun.myuan.slask._

import scala.util.control.Exception


class BaseView() extends HTMLResponse("Index") {
  addLink(
    "stylesheet",
    "https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css",
  )
  addScript("https://code.jquery.com/jquery-3.4.1.slim.min.js")
  addScript("https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js")
  addScript("https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js")

  def newContent(): String = ""

  content =
    s"""<nav class="navbar navbar-expand-lg navbar-light bg-light">
       |  <a class="navbar-brand" href="/">Index</a>
       |  <div class="collapse navbar-collapse" id="navbarSupportedContent">
       |    <ul class="navbar-nav mr-auto">
       |      <li class="nav-item">
       |        <a class="nav-link" href="/">登录</span></a>
       |      </li>
       |      <li class="nav-item">
       |        <a class="nav-link" href="/register">注册</a>
       |      </li>
       |    </ul>
       |  </div>
       |
       |</nav>
       |<div class='container' style='padding: 5%'>${newContent()}</div>
       |""".stripMargin
}

class Index(context: Context, addition: String = "") extends BaseView {
  override def newContent(): String = addition + Components.FormCompt("登录", "Login", showRegister = true)
}

class Register(context: Context, addition: String = "") extends BaseView {
  override def newContent(): String = addition + Components.FormCompt("注册", "Register")
}

class Login(context: Context) extends BaseView {
  override def newContent(): String = {
    try {
      val form = context.form()
      if(Users.checkPassword(form.get("email"), form.get("password"))){
        Components.AlertCompt(
          s"欢迎你 ${form.get("email")}",
          Components.AlertType.success
        )
      } else {
        new Index(context, Components.AlertCompt(
          s"账号或密码错误",
          Components.AlertType.danger
        )).newContent()
      }
    } catch {
      case EmailError(msg) => new Index(
        context, Components.AlertCompt(msg, Components.AlertType.danger)
      ).newContent()
    }
  }
}

class RegisterPost(context: Context) extends BaseView {
  override def newContent(): String = {
    try {
      val form = context.form()
      Users.addUser(form.get("email"), form.get("password"))
      new Index(
        context,
        Components.AlertCompt(
            s"欢迎你 ${form.get("email")}, 你已注册成功, 请登录",
            Components.AlertType.success
        )).newContent()
    } catch {
      case EmailError(msg) => new Register(context, Components.AlertCompt(msg, Components.AlertType.danger)).newContent()
    }
  }
}

case class MainView() {
  var view = new Blueprints
  view.+=(Router("GET", "/", context => new Index(context)))
  view.+=(Router("GET", "/register", context => new Register(context)))
  view.+=(Router("POST", "/Login", context => new Login(context)))
  view.+=(Router("POST", "/Register", context => new RegisterPost(context)))
  view.+=(Router("GET", "/favicon.ico", context => new StaticResponse("favicon.ico")))

}
