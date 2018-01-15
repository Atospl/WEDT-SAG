package agents

import messages.Messages._
import akka.actor.{ Actor, Props, ActorLogging, ActorSystem, ActorRef }
import scala.concurrent.duration._

import scala.io.StdIn

object ScrapersManagerAgent {
  def props(): Props = Props(new ScrapersManagerAgent)
}


class ScrapersManagerAgent extends Actor with ActorLogging {
  /** Map holding references to all working scrapers */
  var scraperActors = Map.empty[String, ActorRef]

  override def preStart(): Unit = {
    log.info("ScrapersManager started...")

  }

  override def receive: Receive = {
    case CreateScraperAgent(url, name, tag, parser) =>
      val scraperName = s"scraper-$name-$tag"
      val scraper = context.actorOf(ScraperAgent.props(url, name, tag, parser), scraperName)
      scraperActors += scraperName -> scraper
      log.info(s"Scraper created " + scraper)
      log.info(s"scrapers created!")

    case OrderScrapping =>
      log.info("Scrapping ordered...")
      scraperActors.foreach(kvpair => kvpair._2 ! "scrape")

    case "runScrapers" =>
      log.info(s"scrapers running!")

  }
}
