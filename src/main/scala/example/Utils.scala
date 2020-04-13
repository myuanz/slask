package example

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.net.{InetSocketAddress, ServerSocket, SocketAddress}


class Context {
  // 触发响应函数的上下文
  var header: Map[String, String] = Map[String, String]()
  var body: String = ""

  def get(key: String): Option[String] = {
    header.get(key)
  }
  def set(key: String, value: String): Unit = {
    header = header ++ Map(key -> value)
  }
}


class HttpClient() {
  var socket: ServerSocket = new ServerSocket
  var blueprints: Array[Blueprints] = new Array[Blueprints](0)

  def run(host: String = "127.0.0.1", port: Int = 5000, timeout: Int = 0): Unit = {

    this.socket.setSoTimeout(timeout)
    this.socket.bind(new InetSocketAddress(host, port))
    while (true) {
      try {
        val socket = this.socket.accept
        println("conn: " + socket.getRemoteSocketAddress)

        val in_buffer = new BufferedReader(new InputStreamReader(socket.getInputStream));

        val first_line = in_buffer.readLine()
        val outputStream = socket.getOutputStream

        (blueprints
          .map(_.matchPath(first_line))
          .reduce(_ ++ _) match {
          case Array(i, _*) => i
          case _ => new ExceptionResponse(NotFound())
        }) match {
          case f: (Context => Response) =>
            val context: Context = new Context()
            var flag = true
            var continuousRNCount: Int = 0

            while (flag) {
              //接收从客户端发送过来的数据
              val line = in_buffer.readLine()

              if (line == null || line == "") {
                flag = false
              } else {
                println("got: " + line)
                val key :: value :: _ = """(.+): (.+)$""".r.findAllMatchIn(line).next.subgroups
                context.set(key, value)
              }
            }
            outputStream.write(f(context).toResponseText.getBytes)
          case er: ExceptionResponse =>
            outputStream.write(er.toResponseText.getBytes)
        }
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

