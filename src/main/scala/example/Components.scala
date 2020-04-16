package example

object Components{

  def FormCompt(title: String, action: String="", showRegister: Boolean=false): String = {
    s"""<h1>${title}</h1>
       |<div class='login-form'>
       |  <form action="${action}" method="post">
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

  object AlertType extends Enumeration {
    type AlertType = Value
    val primary, secondary, success, danger, warning, light, dark = Value//在这里定义具体的枚举实例
  }
  def AlertCompt(message: String, alertType: AlertType.Value): String = {
    ""
  }
}






