package agents

import akka.actor.{ Actor, Props, ActorLogging, ActorSystem, ActorRef }
import scala.io.StdIn

object ScrapersManagerAgent {
  def props(): Props = Props(new ScrapersManagerAgent)
}


class ScrapersManagerAgent extends Actor with ActorLogging {
  var scraperActors = Map.empty[String, ActorRef]



  override def preStart(): Unit = {
    log.info("ScrapersManager started...")

  }


  override def receive: Receive = {
    case "createScraperAgents" =>
      val scraper = context.actorOf(ScraperAgent.props("http://bbc.com", "ScraperBBC"), s"scraper-bbc")
      scraperActors += "BBC" -> scraper
      log.info(s"Scraper created " + scraper)
      log.info(s"scrapers created!")

    case "runScrapers" =>
      log.info(s"scrapers running!")

  }
}
