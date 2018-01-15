package agents

import parsers.bbcParser._
import parsers.cnnParser._

import enums.{Site, Tag}
import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.io.StdIn
import messages.Messages._

object ArtCompSystemApp extends App {
  /** Create /user/ descendants */
  val system = ActorSystem("ArtCompSystem")
  import system.dispatcher

  val scrapersManager = system.actorOf(ScrapersManagerAgent.props(), "scrapersManager")
  val dbAgent = system.actorOf(DBAgent.props(), "dbAgent")
  createScraperAgents(scrapersManager)

  /** Schedule scrapping every 5 minutes */
//  system.scheduler.scheduleOnce(50 milliseconds, scrapersManager, OrderScrapping)
  val scrappingSchedule = system.scheduler.schedule(
                             50 milliseconds,
                             1 minutes,
                             scrapersManager,
                             OrderScrapping) 
//  println("Scrapping scheduled!")



//  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()

  def createScraperAgents(scrapersMgrRef: ActorRef) = {
    println("...Creating scrapers")
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/politics/rss.xml", "bbc", Tag.POLITICS, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", "bbc", Tag.SCIENCE, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/technology/rss.xml", "bbc", Tag.TECHNOLOGY, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_allpolitics.rss", "cnn", Tag.POLITICS, new CnnParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_tech.rss", "cnn", Tag.TECHNOLOGY, new CnnParser)
  }
}
