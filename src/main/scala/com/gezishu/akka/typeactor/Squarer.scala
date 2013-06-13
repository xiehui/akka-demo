package com.gezishu.akka.typeactor

import scala.concurrent.{ Promise, Future, Await }
import akka.actor.{TypedActor, ActorContext,TypedProps}
import akka.actor.ActorSystem
import scala.concurrent.duration._

class SquarerImpl(val name: String) extends Squarer {
  
  def this() = this("default")
//  import TypedActor.dispatcher
  
  def squareDontCare(i: Int) = i * i
  
  def square(i: Int): Future[Int] = Promise.successful(i * i).future
  
  def squareNowPlease(i: Int): Option[Int] = Some(i * i)
  
  def squareNow(i: Int): Int = i * i
  
}

trait Squarer {

  def squareDontCare(i: Int): Unit //fire-forget
  
  def square(i : Int): Future[Int] //非阻塞send-request-reply
  
  def squareNowPlease(i: Int): Option[Int] //阻塞send-request-reply
  
  def squareNow(i: Int): Int //阻塞send-request-reply
}



object SquarerTest extends App {
  
  val system = ActorSystem("mySystem")
  val mySquarer : Squarer = TypedActor(system).typedActorOf(TypedProps[SquarerImpl]())
  mySquarer.squareDontCare(10)
  val oSquarer = mySquarer.squareNowPlease(10)
  val iSquarer = mySquarer.squareNow(10)
  val fSquarer = mySquarer.square(10)
  TypedActor(system).stop(mySquarer)
  
//  system.scheduler.schedule(50 milliseconds) {
//    
//  }
}