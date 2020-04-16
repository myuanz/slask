package fun.myuan.slask

import java.io._

trait ResponseTrait {
  val content_type: String
  var charset: String = "UTF-8"
  var status_code: HttpStatusCodes = OK
  var content_length: Int = buildBody.length
  var server_name: String = "MyuansToyServer"

  type ResponseFunction = (Context) => Response

  def buildFirstLine: String

  def buildHeader: String

  def buildBody: Array[Byte]

  def toResponseBytes: Array[Byte] = (buildHeader + "\r\n\r\n").getBytes("utf-8") ++ buildBody
}

class Response extends ResponseTrait {
  override val content_type: String = ""

  override def buildFirstLine: String = {
    s"HTTP/1.0 ${status_code.statusCode} ${status_code.description}"
  }

  override def buildHeader: String = {
    s"""${buildFirstLine}
       |Content-Length: ${content_length}
       |Content-type: ${content_type}; charset: ${charset}
       |Server: ${server_name}
       |""".stripMargin.replaceAll("\\n", "\\r\\n")
  }

  override def buildBody: Array[Byte] = "Empty Body".getBytes("utf-8")
}

class HTMLResponse(
                    var title: String = "title",
                    var content: String = "",
                    var style: String = "",
                    var script: String = "",
                    var links: Option[Array[String]] = None
                  ) extends Response {
  // HTML响应

  def addLink(rel: String = "", href: String = "", options: Map[String, String] = Map()): Unit = {
    var _options = options

    _options = _options ++ Map("rel" -> _options.getOrElse("rel", rel))
    _options = _options ++ Map("href" -> _options.getOrElse("href", href))

    val optionsStr: String = _options.map(x => s"${x._1}='${x._2}'").mkString(" ")
    val str: String = s"<link ${optionsStr}/>"
    links = Option[Array[String]](
      links.getOrElse(new Array[String](0)) :+ str
    )
  }

  def addScript(src: String = "", options: Map[String, String] = Map()): Unit = {
    var _options = options

    _options = _options ++ Map("src" -> _options.getOrElse("src", src))
    val optionsStr: String = _options.map(x => s"${x._1}='${x._2}'").mkString(" ")
    val str: String = s"<script ${optionsStr}></script>"
    links = Option[Array[String]](
      links.getOrElse(new Array[String](0)) :+ str
    )
  }

  override val content_type: String = "text/html"

  override def buildBody: Array[Byte] =
    s"""<!DOCTYPE html>
       |<html lang="zh-CN">
       |<head>
       |  <meta charset="UTF-8" />
       |  <title>${title}</title>
       |  <style>
       |    ${style}
       |  </style>
       |  <script type="text/javascript">
       |    ${script}
       |  </script>
       |  ${links.getOrElse(new Array[String](0)).mkString("\r\n")}
       |</head>
       |<body>
       |  ${content}
       |</body>
       |""".stripMargin.getBytes("utf-8")
}

class ExceptionResponse(httpStatusCodes: HttpStatusCodes) extends HTMLResponse(
  httpStatusCodes.statusCode.toString, s"<h1>${httpStatusCodes.description}</h1>"
) {}

class JSONResponse(json: String) extends Response {
  // JSON响应
  override val content_type: String = "application/json"

  override def buildBody: Array[Byte] = json.getBytes("utf-8")
}

class StaticResponse(fileName: String, fileType: Option[String] = None) extends Response {
  // 静态文件响应
  val MIMEType: String = fileType.getOrElse(Utils.FileNameToHTTPType(
    fileName.split("[.]").reverse match {
      case Array(i, _*) => i
      case _ => "unknown"
    }
  ))

  override val content_type: String = MIMEType

  override def buildBody: Array[Byte] = {
    val file = new File(System.getProperty("user.dir") + "/" + Config.staticPath + '/' + fileName)
    val in = new FileInputStream(file)
    val bytes = new Array[Byte](file.length.toInt)
    in.read(bytes)
    bytes
  }
}