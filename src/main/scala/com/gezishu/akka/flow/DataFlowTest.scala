package com.gezishu.akka.flow

import scala.concurrent.ExecutionContext.Implicits.global
import akka.dataflow._
import scala.concurrent.Promise
import akka.dispatch._


object DataFlowTest extends App {

  flow {"Hello World!"} onComplete println
  
//  implicit val dispatcher = ...
  
  val v1,v2 = Promise[Int]()
  
//  flow {
//    v1 << v2() + 10
//    v1() + v2()
//  }
}