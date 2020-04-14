package example

import java.io.{BufferedReader, InputStreamReader, OutputStream}
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import fun.myuan.slask._


object Main {
  def main(args: Array[String]): Unit = {
    val port = 20006
    val app = new HttpClient
    app.blueprints = app.blueprints :+ MainView().view
    app.run(port = port)
  }
}
