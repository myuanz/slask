package example

trait ResponseTrait {
  val content_type: String
  var charset: String = "UTF-8"
  var status_code: HttpStatusCodes = OK
  var content_length: Int = buildBody.length
  var server_name: String = "MyuansToyServer"

  type ResponseFunction = (Context) => Response

  def buildFirstLine: String

  def buildHeader: String

  def buildBody: String

  def toResponseText: String = buildHeader + "\r\n\r\n" + buildBody
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
       |""".stripMargin.replaceAll("\n", "\r\n")
  }

  override def buildBody: String = "Empty Body"
}

class HTMLResponse(title: String, var content: String="", style: String = "", script: String = "", links: Option[List[String]]=None) extends Response {
  // HTML响应
  override val content_type: String = "text/html"

  override def buildBody: String =
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
       |  ${links.mkString("\r\n")}
       |</head>
       |<body>
       |  ${content}
       |</body>
       |""".stripMargin
}

class ExceptionResponse(httpStatusCodes: HttpStatusCodes) extends HTMLResponse(
  httpStatusCodes.statusCode.toString, s"<h1>${httpStatusCodes.description}</h1>"
){}

class JSONResponse(json: String) extends Response {
  // JSON响应
  override val content_type: String = "application/json"
  override def buildBody: String = json
}