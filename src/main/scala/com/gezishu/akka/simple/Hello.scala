package com.gezishu.akka.simple

import akka.actor.{ActorSystem,Actor,Props}

object Hello {

  def main(args: Array[String]) {
    val system = ActorSystem()
    system.actorOf(Props[HelloActor]) ! Start
  }
}

case object Start

class HelloActor extends Actor {
  
  val worldActor = context.actorOf(Props[WorldActor])
  
  def receive = {
    case Start => worldActor ! "Hello"
    case x: String => 
      println("Received Message %s".format(x))
      context.system.shutdown
  }
  
}

class WorldActor extends Actor {
  
  def receive = {
    case s: String =>
      sender ! s.toUpperCase + " world!"
  }
}