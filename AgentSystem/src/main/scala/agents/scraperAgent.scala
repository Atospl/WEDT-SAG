package agents

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn

class ScraperAgent extends Actor {

  override def preStart(): Unit = {
    println("Scraper Agent started...")
  }

  override def receive: Receive = {
    case "scrape" =>
      println(s"Scraping...")


  }
}
