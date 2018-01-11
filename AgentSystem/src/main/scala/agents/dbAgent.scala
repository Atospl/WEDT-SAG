package agents

import dbagent.repository
import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn

class DBAgent extends Actor {
  override def preStart(): Unit = {
    println("DBAgent started...")


  }


  override def receive: Receive = {
    case "saveArticle" =>
      println("Saving article...")

    case "getPolitics" =>
      println("Retrieving political articles")

    case "getScience" =>
      println("Retrieving scientifical articles")
  }
}
