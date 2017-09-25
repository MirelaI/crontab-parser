package com.crontab.models

import com.crontab.{ItemParser, config}

sealed trait Item

case class CronTabItem(
  itemType: String,
  cronValues: List[Int]
) extends Item

case class CronTab(
  cronTabItems: List[CronTabItem],
  command: String
) {

  //TODO: improve prettify method
  override def toString: String = {
    val stringifiedItems: List[String] = for (item <- cronTabItems)
     yield s"${item.itemType}: ${item.cronValues}"

    stringifiedItems + s" Command: ${command}"
  }
}

object CronTabItem {
  def apply(itemType: String, expr: String): CronTabItem = {
    val itemValues = new ItemParser(expr, itemType).parse
    // Prettify type
    val typeName = config.Constants.info(itemType)("name").toString

    CronTabItem(typeName,itemValues)
  }
}
