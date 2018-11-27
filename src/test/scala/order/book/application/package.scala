package order.book

import java.io.{ByteArrayInputStream, InputStream}

package object application {

  implicit class StringFunctions(str: String) {
    def toInputStream: InputStream = new ByteArrayInputStream(str.getBytes)
  }

}
