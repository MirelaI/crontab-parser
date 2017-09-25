package com.crontab

case class CronTypeNotFound(message: String) extends Exception(message)
case class CronElementOutsideBounds(message: String) extends Exception(message)
case class ElementNotRecognised(message: String) extends Exception(message)
case class UndefinedValuesForCrontabType(message: String) extends Exception(message)


class ItemParser(val expr: String, elemType: String) {

  private val cronConfig = typeInfoFromConfig(elemType: String)

  def parse: List[Int] = {
    val (instruct: String, skips: Option[String]) = expr.split("/").map {
      _.trim
    } match {
      case Array(int: String, skip: String) => (int, Some(skip))
      case _ => (expr.trim, None)
    }

    val instructionsAsList: List[Int] = instruct.split(",").map {
      _.trim
    } match {
      case l: Array[String] =>
        l.flatMap { elem =>
          // Transform ranges
          if (elem.contains("-")) {
            expandElement(elem)
          } else {
            // Expand * to a range of values within
            // input crontab type
            elem.contains("*") match {
              case true =>
                // Get min and max values from config:
                val info: Map[String, Any] = typeInfoFromConfig(elemType)
                val minValue = info("minValue").toInt
                val maxValue = info("maxValue").toInt

                (minValue to maxValue).toList

              case false => List(parseSingleElem(elem))
            }
          }
        }.toList

      case _ => List(instruct.toInt)
    }

    skips match {
      case Some(x) if x.toInt > 0 =>
        instructionsAsList.filter(p => p % x.toInt == 0)
      case None => instructionsAsList
    }
  }

  private def parseSingleElem(elem: String): Int = {
    // First check if is only digits
    if (isNumber(elem)) {
      // Check if element is within bound
      val isWithinBound = cronConfig("minValue").toInt until
        cronConfig("maxValue").toInt contains
        elem.toInt

      val returnedElem = isWithinBound match {
        case true => elem.toInt
        case false => throw CronElementOutsideBounds(
          s"""
             | ${elem} is outside bounds of ${cronConfig("name")}
           """.stripMargin
          )
      }

      returnedElem
    } else {
      val listedValues = if (cronConfig.get("prettify").isDefined)
        cronConfig("prettify")
      else throw ElementNotRecognised(s"Element ${elem} not recognised")

      listedValues match {
        case s: List[Any] => s.indexOf(elem)
        case _ => throw UndefinedValuesForCrontabType(
          s"""
             |Values of type ${cronConfig} should be a List
           """.stripMargin)
      }
    }
  }

  private def expandElement(elem: String): List[Int] = {
    val elemRange = elem.split("-").map {
      parseSingleElem
    }.toList

    // Check if the min and max of the range are in the
    // element bounds
    val isWithinBound = elemRange.head >= cronConfig("minValue").toInt &&
      elemRange.last <= cronConfig("maxValue").toInt

    val asRange = if (isWithinBound) elemRange.head to elemRange.last
      else throw CronElementOutsideBounds(
        s"""
           |${elem} if outside element bounds:
           |${cronConfig("minValue")} - ${cronConfig("maxValue")}
         """.stripMargin
    )

    asRange.toList
  }

  private def typeInfoFromConfig(cronItemType: String): Map[String, Any] = {
    val info: Option[Map[String, Any]] = config.Constants.info.get(cronItemType)

    if (info.isDefined)
      config.Constants.info(cronItemType)
    else
      throw CronTypeNotFound(
        s"""
           |${cronItemType} does not exist. Available values ${config.Constants.info.keys}
         """.stripMargin
      )
  }

  private def isNumber(input: String) = input forall Character.isDigit
}
