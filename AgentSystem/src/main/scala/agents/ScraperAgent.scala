package agents

import parsers.Parser
import enums.Tag
import java.time.LocalDateTime
import akka.actor.{ Actor, Props, ActorLogging, ActorSystem }
import scala.io.StdIn

object ScraperAgent {
  def props(url: String, name: String, tag: Tag, parserObj: Parser): Props = Props(new ScraperAgent(url, name, tag, parserObj))
}

class ScraperAgent(url: String, name: String, tag: Tag, parserObj: Parser) extends Actor with ActorLogging {
  /** TODO possibly move to constructor - start the system with scraping all articles available,
   *  but then you only want to take things that are newer than ones you've already seen
   */
  var lastTimeUsed = LocalDateTime.of(2017,1,1,0,0)

  override def preStart(): Unit = {
    log.info("Scraper Agent started...")
  }

  override def receive: Receive = {
    case "scrape" =>
      log.info(s"Scraping...")


  }
}
