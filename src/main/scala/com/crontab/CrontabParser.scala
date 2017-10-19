package com.crontab

import com.crontab.models.{CronTab, CronTabItem}

// How a crontab line looks like:
//   */15 0 1,15 * 1-5 /usr/bin/find
// The output should be:
//
// minute 0 15 30 45
// hour 0
// day of month 1 15
// month 1 2 3 4 5 6 7 8 9 10 11 12
// day of week 1 2 3 4 5
// command /usr/bin/find

object InvalidCrontabInput extends Exception

object CrontabParser {

  // We need a way to enforce the order on the
  // crontab elements and parse accordingly to
  // the type of the items.
  private val cronTabOrder = Map(
    0 -> "min",
    1 -> "hour",
    2 -> "dom",
    3 -> "mon",
    4 -> "dow",
    5 -> "cmd"
  )

  def parseCronTabCommand(input: String): CronTab = {
    val cronTabInput = input.split(" ").zipWithIndex.toList

    val parsedCronTab: List[CronTabItem] = for (
      (item, index) <- cronTabInput if index < 5
    ) yield CronTabItem(itemType = cronTabOrder(index), expr = item)

    if (parsedCronTab.length >= 5)
     CronTab(parsedCronTab, cronTabInput.drop(5).map{ _._1 }.mkString(" "))
    else {
      throw InvalidCrontabInput
    }
  }
}