package fun.myuan.slask

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.net.{InetSocketAddress, ServerSocket, SocketAddress}

import scala.util.{Failure, Success}
import scala.concurrent._
import ExecutionContext.Implicits.global

class HttpClient() {
  var socket: ServerSocket = new ServerSocket
  var blueprints: Array[Blueprints] = new Array[Blueprints](0)

  def run(host: String = "127.0.0.1", port: Int = 5000, timeout: Int = 0): Unit = {

    this.socket.setSoTimeout(timeout)
    this.socket.bind(new InetSocketAddress(host, port))
    while (true) {
      try {
        val socket = this.socket.accept
        //        println("conn: " + socket.getRemoteSocketAddress)

        val in_buffer = new BufferedReader(new InputStreamReader(socket.getInputStream));
        val startLine = in_buffer.readLine()
        val outputStream = socket.getOutputStream

        (blueprints
          .map(_.matchPath(startLine))
          .reduce(_ ++ _) match {
          case Array(i, _*) => i
          case _ => new ExceptionResponse(NotFound())
        }) match {
          case f: (Context => Response) =>

            val context: Future[Context] = Future {
              buildContext(in_buffer)
            }
            context onComplete {

              case Success(value) =>
                value.startLine = startLine

                outputStream.write(f(value).toResponseBytes)
                outputStream.close()
                socket.close()
              case Failure(exception) => outputStream.write(exception.getLocalizedMessage.getBytes("utf-8"))
            }
          case er: ExceptionResponse =>
            outputStream.write(er.toResponseBytes)
            outputStream.close()
            socket.close()

        }
      }
      catch {
        case e: Throwable => println(e)
      }
    }
  }

  def buildContext(in_buffer: BufferedReader): Context = {
    var flag = true
    val context: Context = new Context
    var continuousBlankCount = 0
    while (flag) {
      //接收从客户端发送过来的数据
      val line = in_buffer.readLine()

      //      println("got: " + line)
      if (line == null) {
        flag = false
      } else if (line == "") {
        continuousBlankCount += 1
        flag = false
        if (continuousBlankCount == 2)
          flag = false
      } else {
        val key :: value :: _ = """(.+): (.+)$""".r.findAllMatchIn(line).next.subgroups
        context.setHeader(key, value)
        context.sourceText += line + "\r\n"
        continuousBlankCount = 0
      }
    }

    if (context.getHeader("Content-Length") != "") {
      val length = context.getHeader("Content-Length").toInt

      val bytes = new Array[Char](length)
      in_buffer.read(bytes, 0, length)
      context.body = bytes
      context.sourceText += "\r\n" + bytes.mkString
    }

    context
  }
}