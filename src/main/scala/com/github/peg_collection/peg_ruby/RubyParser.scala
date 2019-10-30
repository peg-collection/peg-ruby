package com.github.peg_collection.peg_ruby

import java.io.FileReader
object RubyParser extends Ruby {
  def main(args: Array[String]): Unit = {
    val reader = new FileReader(args(0))
    println(parseAll(program, reader))
  }
}
