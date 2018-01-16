package agents

import parsers.Parser
import enums.Tag
import messages.Messages._
import java.sql.Timestamp
import java.time.LocalDateTime

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import HttpService.HttpService
import scala.io.StdIn

object ScraperAgent {
  def props(url: String, name: String, tag: Tag, parserObj: Parser): Props = Props(new ScraperAgent(url, name, tag, parserObj))
}

class ScraperAgent(url: String, name: String, tag: Tag, parserObj: Parser) extends Actor with ActorLogging {
  /** Start the system with scraping all articles available,
    * but then you only want to take things that are newer than ones you've already seen
    */
  var lastTimeUsed = LocalDateTime.of(2017, 1, 1, 0, 0)

  override def preStart(): Unit = {
    log.info("Scraper Agent started...")
  }

  override def receive: Receive = {
    case Scrap =>
      var articleList = parserObj.parse(url, tag)
      articleList = articleList.filter(x => lastTimeUsed.isBefore(x.publishedDate.toLocalDateTime()))
      log.info(articleList.length + " articles found!")
      lastTimeUsed = LocalDateTime.now()
      articleList.foreach { elem => {
          if (articleList.length > 0) {
            context.actorSelection("/user/dbAgent") ! SaveArticle(elem)
          }
        }
      }
  }
}
