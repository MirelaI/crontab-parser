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

  override def toString: String = {
    val stringifiedItems: List[String] = for (item <- cronTabItems)
     yield s"${item.itemType}: ${item.cronValues.mkString(",")}"

    stringifiedItems.mkString("\n") + s"\nCommand: ${command}"
  }
}

object CronTabItem {
  def apply(itemType: String, expr: String): CronTabItem = {
    val itemValues = new ItemParser(expr, itemType).parse

    val typeName = config.Constants.info(itemType)("name").toString

    CronTabItem(typeName, itemValues)
  }
}
