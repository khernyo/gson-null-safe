package khernyo.gson.nullsafe

import org.scalatest.{Assertions, FunSuite}
import com.google.gson._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import com.google.common.base.Optional

@RunWith(classOf[JUnitRunner])
class NullSafeTest extends FunSuite with Assertions {
  val gson =
    new GsonBuilder()
      .registerTypeAdapterFactory(new NullSafeTypeAdapterFactory)
      .create()

  def parse[T](json: String)(implicit m: Manifest[T]) = gson.fromJson(json, m.runtimeClass)

  test("NullSafeOptional and NullSafeNullable annotation together") {
    intercept[IllegalArgumentException](parse[DataOptionalNullable]("""{}"""))
  }

  test("primitive int") {
    intercept[IllegalArgumentException](parse[DataPrimitiveInt]("""{i: 1}"""))
    intercept[IllegalArgumentException](parse[DataPrimitiveIntNullable]("""{i: 1}"""))
    intercept[IllegalArgumentException](parse[DataPrimitiveIntOptional]("""{i: 1}"""))
  }

  test("Integer") {
    intercept[JsonParseException](parse[DataInteger]("""{}"""))
    intercept[JsonParseException](parse[DataInteger]("""{"i": null}"""))
    assert(parse[DataInteger]("""{"i": 1}""") === new DataInteger(1))

    assert(parse[DataIntegerNullable]("""{}""") === new DataIntegerNullable(null))
    assert(parse[DataIntegerNullable]("""{"i": null}""") === new DataIntegerNullable(null))
    assert(parse[DataIntegerNullable]("""{"i": 1}""") === new DataIntegerNullable(1))

    assert(parse[DataIntegerOptional]("""{}""") === new DataIntegerOptional(Optional.absent()))
    assert(parse[DataIntegerOptional]("""{"i": null}""") === new DataIntegerOptional(Optional.absent()))
    assert(parse[DataIntegerOptional]("""{"i": 1}""") === new DataIntegerOptional(Optional.of(1)))
  }

  test("String") {
    intercept[JsonParseException](parse[DataString]("""{}"""))
    intercept[JsonParseException](parse[DataString]("""{"s": null}"""))
    assert(parse[DataString]("""{"s": "e"}""") === new DataString("e"))

    assert(parse[DataStringNullable]("""{}""") === new DataStringNullable(null))
    assert(parse[DataStringNullable]("""{"s": null}""") === new DataStringNullable(null))
    assert(parse[DataStringNullable]("""{"s": "e"}""") === new DataStringNullable("e"))

    assert(parse[DataStringOptional]("""{}""") === new DataStringOptional(Optional.absent()))
    assert(parse[DataStringOptional]("""{"s": null}""") === new DataStringOptional(Optional.absent()))
    assert(parse[DataStringOptional]("""{"s": "e"}""") === new DataStringOptional(Optional.of("e")))
  }
}
