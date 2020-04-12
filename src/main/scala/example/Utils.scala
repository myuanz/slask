package example

import java.io.{BufferedReader, InputStream, InputStreamReader, OutputStream}
import java.net.{InetSocketAddress, ServerSocket, SocketAddress}

import scala.util.matching.Regex


class Context{
  // 触发响应函数的上下文
  val attributes: Map[String, String] = Map[String, String]()
  def get(key: String): Option[String] ={
    attributes.get(key)
  }
}
trait ResponseTrait{
  val content_type: String
  var charset: String = "UTF-8"
  var status_code: T >: HttpStatusCodes = OK
  var content_length: Int = buildBody.length

  def buildFirstLine: String
  def buildHeader: String
  def buildBody: String
  def toResponseText: String = buildHeader + "\r\n\r\n" + buildBody
}
class Response extends ResponseTrait{
  override val content_type: String = ""

  override def buildFirstLine: String = {
    s"HTTP/1.0 ${status_code} OK"
  }
  override def buildHeader: String = {
    s"""${buildFirstLine}
       |Content-Length: ${content_length}
       |Content-type: ${content_type}; charset: ${charset}
       |
       |""".stripMargin
  }

  override def buildBody: String = ???

}
case class HTMLResponse() extends Response{
  // HTML响应
  override val content_type: String = "text/html"
  var title: String = ""

}


case class JSONResponse() extends Response{
  // JSON响应
  override val content_type: String = "application/json"
}

trait ResponseTrait {
  type ResponseFunction = (Context) => Response
}

case class Router(method: String, path: String, responseFunction: (Context) => Response) {}

class Blueprints extends ResponseTrait {
  var routers: Array[Router] = new Array[Router](0)

  def matchPath(startLine: String): ResponseFunction = {
    val reg: Regex = """([A-Z]+) (/.*) HTTP/\d\.\d$""".r
    val method::path::_ = reg.findAllMatchIn("GET / HTTP/1.1").next.subgroups
    for (router <- routers){
      router match {
        case Router(router.method, router.path, _) => router.responseFunction
        case Router(_, router.path, _) =>
      }
    }
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
        while (flag) {
          //接收从客户端发送过来的数据
          val str = in_buffer.readLine();
          if (str == null || str == "") {
            flag = false
          } else {
            println("got: " + str)
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

