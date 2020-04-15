package example


import fun.myuan.slask._
import java.net.URLDecoder


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
       |  <a class="navbar-brand" href="#">Index</a>
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
class Form(title: String, action: String="", showRegister: Boolean=false) {
  def basicForm: String =
    s"""<h1>${title}</h1>
      |<div class='login-form'>
      |  <form action="${action}" method="get">
      |  <div class="form-group">
      |    <label for="email">邮箱</label>
      |    <input type="email" class="form-control" id="email" name="email" aria-describedby="emailHelp" placeholder="name@example.com" value="admin@myuan.fun">
      |  </div>
      |  <div class="form-group">
      |    <label for="password">密码</label>
      |    <input type="password" class="form-control" id="password" name="password" required>
      |  </div>
      |  <button type="submit" class="btn btn-primary">${title}</button>
      |  ${if (showRegister) "<text>没有账号? <a href='register'>注册</a></text>" else ""}
      |  </form>
      |</div>
      |""".stripMargin
}

class Index(context: Context) extends BaseView {
  override def newContent(): String = new Form("登录", "Login", showRegister = true).basicForm
}
class Register(context: Context) extends BaseView {
  override def newContent(): String = new Form("注册", "Register").basicForm
}

class Login(context: Context) extends BaseView {
  var _newContent = "未找到数据"
  if (context.body != ""){
    val email::password::_ = context.body.split("[&]").map(URLDecoder.decode(_, "utf-8")).toList
    _newContent = s"登录成功, ${email}, ${password}"
  }

  override def newContent(): String = _newContent
}
class RegisterPost(context: Context) extends BaseView {
  override def newContent(): String = "注册成功"
}
case class MainView() {
  var view = new Blueprints
  view.+=(Router("GET", "/", context => new Index(context)))
  view.+=(Router("GET", "/register", context => new Register(context)))
  view.+=(Router("POST", "/Login", context => new Login(context)))
  view.+=(Router("POST", "/Register", context => new RegisterPost(context)))
  view.+=(Router("GET", "/favicon.ico", context => new StaticResponse("favicon.ico")))

}
