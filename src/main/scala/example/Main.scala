package example

import java.io.{BufferedReader, InputStreamReader, OutputStream}
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import example.HttpClient


object Main {
  def main(args: Array[String]): Unit = {
    val port = 20006
    val app = new HttpClient
    app.run(port = port)
  }
}
