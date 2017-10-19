package com.crontab

import com.crontab.models.{CronTab, CronTabItem}
import org.scalatest._

class CronTabParserTest extends FlatSpec with Matchers {

  // Test * * * * MON ls

  "CrontabParser" should "parse correctly" in  {
    val command = "* * * * MON ls -la"
    val expandedCommand: CronTab = CrontabParser.parseCronTabCommand(command)

    val expectedResult = CronTab(
      List(
        CronTabItem("Minutes", (0 to 59).toList),
        CronTabItem("Hours", (0 to 23).toList),
        CronTabItem("Day of Month", (0 to 31).toList),
        CronTabItem("Month", (1 to 12).toList),
        CronTabItem("Day of Week", List(1))
      ),
      "ls -la"
    )

    assert(expectedResult == expandedCommand)
  }
}
