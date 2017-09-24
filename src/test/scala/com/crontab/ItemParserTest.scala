package com.crontab

import org.scalatest._
import com.crontab.CronTypeNotFound

class ItemParserTest extends FlatSpec with Matchers {

  "A ItemParser" should "require expression string as an input" in {
    val executor = new ItemParser("*/15")
    executor.parse("min")
  }

  it should "throw NumberFormatException wrong arguments are passed" in {
    val executor = new ItemParser("a")
    a [NumberFormatException] should be thrownBy {
      executor.parse("min")
    }
  }

  it should "throw NoSuchElementException if parser type does not exist" in {
    val executor = new ItemParser("*/15")
    a [CronTypeNotFound] should be thrownBy {
      executor.parse("day")
    }
  }
}
