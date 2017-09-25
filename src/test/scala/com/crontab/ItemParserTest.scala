package com.crontab

import org.scalatest._

class ItemParserTest extends FlatSpec with Matchers {

  "A ItemParser" should "require expression string as an input" in {
    val executor = new ItemParser("*/15", "min")
    executor.parse
  }

  it should "throw ElementNotRecognised wrong arguments are passed" in {
    val executor = new ItemParser("a", "min")
    a [ElementNotRecognised] should be thrownBy {
      executor.parse
    }
  }

  it should "throw CronTypeNotFound if parser type does not exist" in {
    a [CronTypeNotFound] should be thrownBy {
      val executor = new ItemParser("*/15", "day")
    }
  }

  it should "parse correctly a number" in {
    val executor = new ItemParser("15", "min")
    val result = executor.parse
    assert(result.head == 15)
  }

  it should "parse correctly a range of numbers" in {
    val executor = new ItemParser("15-16", "min")
    val result = executor.parse
    assert(result == List(15,16))
  }

  it should "throw an error if number is outside expected range" in {
    val executor = new ItemParser("59-60", "min")
    a [CronElementOutsideBounds] should be thrownBy {
      executor.parse
    }
  }

  it should "correctly parse week day literals" in {
    val executor = new ItemParser("SUN", "dow")
    val result = executor.parse
    assert(result.head == 0)
  }

  it should "throw an exception if literals are provided to wrong type" in {
    val executor = new ItemParser("SUN", "min")
    a [ElementNotRecognised] should be thrownBy {
      executor.parse
    }
  }

  it should "correctly parse range of week day literals" in {
    val executor = new ItemParser("SUN-MON", "dow")
    val result = executor.parse
    assert(result == List(0,1))
  }

  it should "correctly parse *" in {
    val executor = new ItemParser("*", "dow")
    val result = executor.parse
    assert(result == List(0, 1, 2, 3, 4, 5, 6))
  }
}
