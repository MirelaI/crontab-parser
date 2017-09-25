package com

package object crontab {
  implicit class RichAny(s: Any) {
    def toInt = s.toString.toInt
  }
}
