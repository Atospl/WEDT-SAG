package agents

import akka.actor.{ Actor, Props, ActorLogging, ActorSystem }
import scala.io.StdIn

object ScraperAgent {
  def props(url: String, name: String, tag: String): Props = Props(new ScraperAgent(url, name, tag))
}

class ScraperAgent(url: String, name: String, tag: String) extends Actor with ActorLogging {

  override def preStart(): Unit = {
    log.info("Scraper Agent started...")
  }

  override def receive: Receive = {
    case "scrape" =>
      log.info(s"Scraping...")


  }
}
