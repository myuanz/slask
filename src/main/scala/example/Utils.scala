package example

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.net.{InetSocketAddress, ServerSocket, SocketAddress}


class Context {
  // 触发响应函数的上下文
  val attributes: Map[String, String] = Map[String, String]()

  def get(key: String): Option[String] = {
    attributes.get(key)
  }
}


class HttpClient() {
  var socket: ServerSocket = new ServerSocket
  var routes: Array[Router] = new Array[Router](0)

  def run(host: String = "127.0.0.1", port: Int = 5000, timeout: Int = 0): Unit = {

    this.socket.setSoTimeout(timeout)
    this.socket.bind(new InetSocketAddress(host, port))
    while (true) {
      try {
        val socket = this.socket.accept
        println("conn: " + socket.getRemoteSocketAddress)

        val in_buffer = new BufferedReader(new InputStreamReader(socket.getInputStream));
        val out_buffer = new StringBuilder
        out_buffer.append("HTTP/1.0 200 OK\r\n\r\n")
        out_buffer.append("<h1>header</h1>")

        var flag = true
        var line_count = 0
        while (flag) {
          //接收从客户端发送过来的数据
          val str = in_buffer.readLine()
          if (str == null || str == "") {
            flag = false
          } else {
            println("got: " + str)
            if (line_count==0){

            }
            out_buffer.append(str + "<br/>")
          }
        }
        val outputStream = socket.getOutputStream
        outputStream.write(out_buffer.toString.getBytes)
        outputStream.close()
        socket.close()
      }
      catch {
        case e: Throwable => println(e)
      }
    }
  }

}

class PageBuilder {

}

