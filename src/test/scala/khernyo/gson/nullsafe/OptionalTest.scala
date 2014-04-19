package khernyo.gson.nullsafe

import org.scalatest.{Assertions, FunSuite}
import com.google.gson.{JsonParseException, GsonBuilder}
import com.google.common.base.Optional
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class OptionalTest extends FunSuite with Assertions {
  val gsonBuilder =new GsonBuilder().registerTypeAdapterFactory(new OptionalTypeAdapterFactory())
  val gson = gsonBuilder.create()
  val gsonWithNulls = gsonBuilder.serializeNulls().create()

  def parse[T](json: String)(implicit m: Manifest[T]): T = gson.fromJson[T](json, m.runtimeClass)

  test("read") {
    assert(parse[DataOptional]("""{}""").i === null)
    assert(parse[DataOptional]("""{"i": null}""") === new DataOptional(Optional.absent()))
    assert(parse[DataOptional]("""{"i": 1}""") === new DataOptional(Optional.of(1)))
    intercept[JsonParseException](parse[DataOptional]("""{"i": "s"}"""))
  }

  test("write without nulls") {
    assert(gson.toJson(new DataOptional(Optional.absent())) === """{}""")
    assert(gson.toJson(new DataOptional(Optional.of(1))) === """{"i":1}""")
  }

  test("write with nulls") {
    assert(gsonWithNulls.toJson(new DataOptional(Optional.absent())) === """{"i":null}""")
  }
}
