package ml.combust.mleap.binary

import java.io.{ByteArrayOutputStream, DataOutputStream}
import java.nio.charset.Charset

import ml.combust.mleap.runtime.LeapFrame
import ml.combust.mleap.runtime.serialization.{BuiltinFormats, FrameWriter}
import ml.combust.mleap.json.JsonSupport._
import spray.json._
import resource._

import scala.util.Try

/**
  * Created by hollinwilkins on 11/2/16.
  */
class DefaultFrameWriter[LF <: LeapFrame[LF]](frame: LF) extends FrameWriter {
  override def toBytes(charset: Charset = BuiltinFormats.charset): Try[Array[Byte]] = {
    (for(out <- managed(new ByteArrayOutputStream())) yield {
      val serializers = frame.schema.fields.map(_.dataType).map(ValueSerializer.serializerForDataType)
      val dout = new DataOutputStream(out)
      val schemaBytes = frame.schema.toJson.prettyPrint.getBytes(BuiltinFormats.charset)
      dout.writeInt(schemaBytes.length)
      dout.write(schemaBytes)
      dout.writeInt(frame.dataset.size)

      for(row <- frame.dataset) {
        var i = 0
        for(s <- serializers) {
          s.write(row(i), dout)
          i = i + 1
        }
      }

      dout.flush()
      out.toByteArray
    }).tried
  }
}
