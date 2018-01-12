package agents

import dbagent.repository
import akka.actor.{ Actor, Props, ActorLogging, ActorSystem }
import scala.io.StdIn

object DBAgent {
  def props(): Props = Props(new DBAgent)
}

class DBAgent extends Actor with ActorLogging {
  override def preStart(): Unit = {
    log.info("DBAgent started...")


  }


  override def receive: Receive = {
    case "saveArticle" =>
      log.info("Saving article...")

    case "getPolitics" =>
      log.info("Retrieving political articles")

    case "getScience" =>
      log.info("Retrieving scientifical articles")
  }
}
