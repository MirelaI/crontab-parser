package com.crontab

case object CronTypeNotFound extends Exception

class ItemParser(val expr: String) {

  def parse(elemType: String): List[Int] = {
    val (instruct: String, skips: Option[String]) = expr.split("/").map {
      _.trim
    } match {
      case Array(int: String, skip: String) => (int, Some(skip))
      case _ => (expr.trim, None)
    }

    val instructionsAsList: List[List[Int]] = instruct.split(",").map {
      _.trim
    } match {
      case l: Array[String] =>
        l.map(elem =>
          // Transform ranges
          if (elem.contains("-")) {
            val elemRange = elem.split("-").map {
              _.toInt
            }.toList
            val asRange = elemRange.head to elemRange.last
            asRange.toList
          } else {
            elem.contains("*") match {
              case true =>
                // Get min and max values from config:
                val info: Map[String, Any] = typeInfoFromConfig(elemType)
                val minValue = info("minValue").toString.toInt
                val maxValue = info("maxValue").toString.toInt

                (minValue to maxValue).toList

              case false => List(elem.toInt)
            }
          }
        ).toList
      case _ => List(List(instruct.toInt))
    }

    val flattenValues = instructionsAsList.flatten
    skips match {
      case Some(x) if x.toInt > 0 =>
        flattenValues.filter(p => p % x.toInt == 0)
      case None => flattenValues
    }
  }

  private def typeInfoFromConfig(crontItemType: String): Map[String, Any] = {
    val info: Option[Map[String, Any]] = config.Constants.info.get(crontItemType)

    info match {
      case Some(typeInfo) => typeInfo
      case None => throw CronTypeNotFound
    }
  }
}


