package agents

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn

class ScrapersManagerAgent extends Actor {
  override def preStart(): Unit = {
    println("ScrapersManager started...")

  }


  override def receive: Receive = {
    case "createScraperAgents" =>
      println(s"scrapers created!")

    case "runScrapers" =>
      println(s"scrapers running!")

  }
}
