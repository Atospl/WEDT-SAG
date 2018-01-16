package agents

import parsers.bbcParser._
import parsers.cnnParser._

import enums.{Site, Tag}
import akka.actor.{ Actor, ActorRef, Props, ActorSystem }
import scala.concurrent.duration._
import scala.language.postfixOps
import dbagent.repository.ArticleRepository
import scala.io.StdIn
import java.time.LocalDate
import java.time.LocalDateTime
import messages.Messages._
import scala.util.control.Breaks._

object ArtCompSystemApp extends App {
  /** Create /user/ descendants */
  val system = ActorSystem("ArtCompSystem")
  import system.dispatcher

  val scrapersManager = system.actorOf(ScrapersManagerAgent.props(), "scrapersManager")
  val dbAgent = system.actorOf(DBAgent.props(), "dbAgent")
  createScraperAgents(scrapersManager)

  /** Schedule scrapping every 5 minutes */
  val scrappingSchedule = system.scheduler.schedule(
                             50 milliseconds,
                             1 minutes,
                             scrapersManager,
                             OrderScrapping) 
  handleUser()
//  println(">>> Press ENTER to exit <<<")
  
  try StdIn.readLine()
  finally system.terminate()
  
  def handleUser() = {
    var userTagInt: Int = -1
    var tagMap = Map.empty[Int, Tag]  
    /** values provided by the user */
    var userTag: Tag = Tag.TECHNOLOGY
    var dateFrom: LocalDate = LocalDate.now()
    var dateTo: LocalDate = LocalDate.now()
    //////////////////////////////////////////////
    
    /** Getting tag from user */
    var tagRange = Tag.values.zipWithIndex
    do {
      println("Choose thematics of article you're interested in:")
      for((d, i) <- tagRange) {
        println("[%d] %s".format(i, d)) 
        tagMap += i -> d
      }
      try userTagInt = StdIn.readInt()
      catch { case e: Throwable => {} }
    }
    while(!(0 to tagRange.length - 1).contains(userTagInt))
    println("Okay!")
    userTag = tagMap(userTagInt)
    /** Getting dates of articles from user */
    var datesAllright = false
    do {
      println("Write date from which articles shall be considered")
      println("Eg. 2017-12-31")
      try {
        dateFrom = LocalDate.parse(StdIn.readLine())
        datesAllright = true
      }
      catch { case e: Throwable => {} }
    }
    while(!datesAllright)
    datesAllright = false
    do {
      println("Write date from which articles shall be considered")
      try {
        dateTo = LocalDate.parse(StdIn.readLine())
        datesAllright = true
      }
      catch { case e: Throwable => {} }
    }
    while(!datesAllright)
    
    /** Selecting article */
    var articlesOption = ArticleRepository.getByTagName(userTag.name)
    var articles: List[dbagent.models.ArticleModel] = null
    if(articles.size > 0)
      articles = articlesOption.get
    else
    {
      println("Articles not found")
      system.terminate()
    }
    // get rid of younger than dateFrom and older than date to
    articles = articles.filter(x => 
        x.publishedDate.toLocalDateTime().isAfter(dateFrom.atStartOfDay())  &&
        x.publishedDate.toLocalDateTime().isBefore(dateTo.atStartOfDay()))
    print(articles.length)
}

  def createScraperAgents(scrapersMgrRef: ActorRef) = {
    println("...Creating scrapers")
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/politics/rss.xml", "bbc", Tag.POLITICS, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", "bbc", Tag.SCIENCE, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/technology/rss.xml", "bbc", Tag.TECHNOLOGY, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_allpolitics.rss", "cnn", Tag.POLITICS, new CnnParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_tech.rss", "cnn", Tag.TECHNOLOGY, new CnnParser)
  }

}
