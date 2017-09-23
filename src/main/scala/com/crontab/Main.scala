package com.crontab

/*
  Main object to run the CronParser application:
  Input arguments: crontab command
*/

object Main extends App {

  override def main(args: Array[String]): Unit = {
    Console.println(
      "Output: " + CrontabParser.parseCronTabCommand(args(0))
    )
  }
}
