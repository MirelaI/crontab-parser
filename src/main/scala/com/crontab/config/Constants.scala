package com.crontab.config

object Constants {
  private val weekDays = Seq("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT")
  private val months = Seq(
    "JAN", "FEB", "MAR", "APR", "IUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
  )

  val info = Map(
    "min" -> Map(
      "name" -> "Minutes",
      "maxValue" -> 59,
      "minValue" -> 0
    ),
    "hour" -> Map(
      "name" -> "Hours",
      "maxValue" -> 23,
      "minValue" -> 0
    ),
    "dom" -> Map(
      "name" -> "Day of Month",
      "maxValue" -> 31,
      "minValue" -> 0
    ),
    "mon" -> Map(
      "name" -> "Month",
      "maxValue" -> 12,
      "minValue" -> 1,
      "prettify" -> months
    ),
    "dow" -> Map(
      "name" -> "Day of Week",
      "maxValue" -> 6,
      "minValue" -> 0,
      "prettify" -> weekDays
    )
  )
}
