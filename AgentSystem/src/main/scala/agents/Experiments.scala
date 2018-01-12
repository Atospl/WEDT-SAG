package agents

import parsers.bbcParser._
import parsers.cnnParser._

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import scala.io.StdIn
import messages.Messages._

object ArtCompSystemApp extends App {
  val system = ActorSystem("ArtCompSystem")

  val scrapersManager = system.actorOf(ScrapersManagerAgent.props(), "scrapersManager")
  val dbAgent = system.actorOf(DBAgent.props(), "dbAgent")
  createScraperAgents(scrapersManager)
  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()

  def createScraperAgents(scrapersMgrRef: ActorRef) = {
    println("...Creating scrapers")
    scrapersMgrRef ! CreateScraperAgent("bbc.com", "bbc", "politics", new BbcParser)

  }
}
