package agents

import messages.Messages._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{ Actor, Props, ActorLogging, ActorSystem, ActorRef }
import scala.concurrent.duration._
import com.typesafe.config.ConfigFactory
import java.io.File
import scala.io.Source
import scala.io.StdIn
import scala.util.control.Breaks._
import scala.concurrent.duration._
import scala.language.postfixOps

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
      log.info("Scraping ordered...")
      scraperActors.foreach(kvpair => kvpair._2 ! Scrap)

    case "runScrapers" =>
      log.info(s"scrapers running!")

  }
}

object ScrapersManager {
  val config = ConfigFactory.parseResources("remote_application.conf")
  val system = ActorSystem("ArtCompSystem", config)


  def main(args: Array[String]) {
    val remote = system.actorOf(ScrapersManagerAgent.props(), "scrapersManager")
    val scrappingSchedule = system.scheduler.schedule(
                              50 milliseconds,
                              3 minutes,
                              remote,
                              OrderScrapping)
    println("Remote ScrapersManager is ready!")
  }
}
