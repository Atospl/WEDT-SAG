package agents

import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import scala.io.StdIn
import messages.Messages._

object ArtCompSystemApp extends App {
  val system = ActorSystem("ArtCompSystem")
  println("Hello, world!")

  val scrapersManager = system.actorOf(ScrapersManagerAgent.props(), "scrapersManager")
  val dbAgent = system.actorOf(DBAgent.props(), "dbAgent")
  println(s"DBAgent: $dbAgent, ScrapersManager: $scrapersManager");
  createScraperAgents(scrapersManager)
  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()

  def createScraperAgents(scrapersMgrRef: ActorRef) = {
    println("...Creating scrapers")
    scrapersMgrRef ! CreateScraperAgent("bbc.com", "bbc", "politics")

  }
}
