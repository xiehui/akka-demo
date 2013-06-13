package com.gezishu.akka

import akka.actor.Actor
import akka.event.Logging
import akka.actor.Props
import akka.actor.ActorSystem
import scala.concurrent.duration._

class MyActor extends Actor {

  val log = Logging(context.system, this)
  
  def receive = {
    case "test" => log.info("received message test")
    case _ => log.info("received unknown message")
  }
  
}

class FirstActor extends Actor {
  
  import context._
  
  val myActor = actorOf(Props[MyActor], "firstsubactor")
  
  def receive = {
    case x => myActor ! x
  }
  
}

object Main extends App {
  
  val system = ActorSystem("mySystem")
  val myActor = system.actorOf(Props[MyActor], "myactor")
  
  myActor ! "test"
  myActor ! ""
  
}