package fun.myuan.slask

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

object Utils {
  def FileNameToHTTPType(fileName: String): String = {
    /*
    引自: https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
     */
    val extName: String = fileName.split(".").reverse match {
      case Array(i, _*) => i
      case _ => fileName
    }

    val basicMIMETypesMap: Map[String, String] = Map(
      "aac" -> "audio/aac",
      "abw" -> "application/x-abiword",
      "arc" -> "application/x-freearc",
      "avi" -> "video/x-msvideo",
      "azw" -> "application/vnd.amazon.ebook",
      "bin" -> "application/octet-stream",
      "bmp" -> "image/bmp",
      "bz" -> "application/x-bzip",
      "bz2" -> "application/x-bzip2",
      "csh" -> "application/x-csh",
      "css" -> "text/css",
      "csv" -> "text/csv",
      "doc" -> "application/msword",
      "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "eot" -> "application/vnd.ms-fontobject",
      "epub" -> "application/epub+zip",
      "gif" -> "image/gif",
      "htm" -> "text/html",
      "html" -> "text/html",
      "ico" -> "image/vnd.microsoft.icon",
      "ics" -> "text/calendar",
      "jar" -> "application/java-archive",
      "jpeg" -> "image/jpeg",
      "jpg" -> "image/jpeg",
      "js" -> "text/javascript",
      "json" -> "application/json",
      "jsonld" -> "application/ld+json",
      "mid" -> "audio/midi audio/x-midi",
      "midi" -> "audio/midi audio/x-midi",
      "mjs" -> "text/javascript",
      "mp3" -> "audio/mpeg",
      "mpeg" -> "video/mpeg",
      "mpkg" -> "application/vnd.apple.installer+xml",
      "odp" -> "application/vnd.oasis.opendocument.presentation",
      "ods" -> "application/vnd.oasis.opendocument.spreadsheet",
      "odt" -> "application/vnd.oasis.opendocument.text",
      "oga" -> "audio/ogg",
      "ogv" -> "video/ogg",
      "ogx" -> "application/ogg",
      "otf" -> "font/otf",
      "png" -> "image/png",
      "pdf" -> "application/pdf",
      "ppt" -> "application/vnd.ms-powerpoint",
      "pptx" -> "application/vnd.openxmlformats-officedocument.presentationml.presentation",
      "rar" -> "application/x-rar-compressed",
      "rtf" -> "application/rtf",
      "sh" -> "application/x-sh",
      "svg" -> "image/svg+xml",
      "swf" -> "application/x-shockwave-flash",
      "tar" -> "application/x-tar",
      "tif" -> "image/tiff",
      "tiff" -> "image/tiff",
      "ttf" -> "font/ttf",
      "txt" -> "text/plain",
      "vsd" -> "application/vnd.visio",
      "wav" -> "audio/wav",
      "weba" -> "audio/webm",
      "webm" -> "video/webm",
      "webp" -> "image/webp",
      "woff" -> "font/woff",
      "woff2" -> "font/woff2",
      "xhtml" -> "application/xhtml+xml",
      "xls" -> "application/vnd.ms-excel",
      "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      "xml" -> "text/xml",
      "xul" -> "application/vnd.mozilla.xul+xml",
      "zip" -> "application/zip",
      "3gp" -> "video/3gpp",
      "3g2" -> "video/3gpp2",
      "7z" -> "application/x-7z-compressed"
    )
    basicMIMETypesMap.getOrElse(extName, "application/octet-stream")
  }
}
