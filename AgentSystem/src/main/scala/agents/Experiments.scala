package agents

import parsers._
import parsers.bbcParser._
import parsers.cnnParser._
import parsers.nytimesParser._
import parsers.washingtontimesParser._

import enums.{Site, Tag}
import akka.actor.{ Actor, ActorRef, ActorSelection, Props, ActorSystem }
import scala.concurrent.duration._
import scala.language.postfixOps
import dbagent.repository.ArticleRepository
import scala.io.StdIn
import java.time.LocalDate
import java.time.LocalDateTime
import messages.Messages._
import scala.util.control.Breaks._
import HttpService.AppHttpService

object ArtCompSystemApp extends App {
  /** Create /user/ descendants */
  val system = ActorSystem("ArtCompSystem")
  import system.dispatcher
  val scrapersManager = system.actorSelection("akka.tcp://ArtCompSystem@127.0.0.1:2553/user/scrapersManager")
  val dbAgent = system.actorOf(DBAgent.props(), "dbAgent")
  createScraperAgents(scrapersManager)
  while(true)
    handleUser()

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
      println("\n\nWelcome to Article Comparison App!")
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
      println("Write date until which articles shall be considered")
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

    var article: dbagent.models.ArticleModel = null
    if(!articlesOption.isEmpty){
      var userArtInt = -1
      articles = articlesOption.get
      // get rid of younger than dateFrom and older than date to
      articles = articles.filter(x =>
        x.publishedDate.toLocalDateTime().isAfter(dateFrom.atStartOfDay())  &&
          x.publishedDate.toLocalDateTime().isBefore(dateTo.atStartOfDay()))

      if(articles.size == 0) {
        println("Articles not found")
        system.terminate()
      }
      do {
        println("Choose article to find most similar to:")
        for((art, i) <- articles.zipWithIndex) {
          println("[%d], %s".format(i, art.title))
        }
        try userArtInt = StdIn.readInt()
        catch { case e: Throwable => {} }
      }
      while(!(0 to articles.length - 1).contains(userArtInt))
      article = articles(userArtInt)
    }
    else
    {
      println("Articles not found")
      system.terminate()
    }

    /** Showing top 10 similar articles*/
      try {
        var similar = AppHttpService.getSimilar(article, articles)
        if(similar.size > 0) {
          println("Similar articles: ")
        }
        if(similar.size > 11) {
          similar = similar.slice(0, 11)
        }
        similar.foreach(elem => {
          val id = elem.id
          val title = articles.find(_.id.toString == id).get.title
          val url = articles.find(_.id.toString == id).get.url
          println("[%s], %s \n %s".format(title, elem.similarity, url))
        })
      }
      catch { case e: Throwable => {} }
}

  def createScraperAgents(scrapersMgrRef: ActorSelection) = {
    println("...Creating scrapers")
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/politics/rss.xml", "bbc", Tag.POLITICS, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", "bbc", Tag.SCIENCE, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.bbci.co.uk/news/technology/rss.xml", "bbc", Tag.TECHNOLOGY, new BbcParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_allpolitics.rss", "cnn", Tag.POLITICS, new CnnParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.cnn.com/rss/cnn_tech.rss", "cnn", Tag.TECHNOLOGY, new CnnParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.nytimes.com/services/xml/rss/nyt/World.xml", "nytimes", Tag.POLITICS, new NytimesParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.nytimes.com/services/xml/rss/nyt/Technology.xml", "nytimes", Tag.TECHNOLOGY, new NytimesParser)
    scrapersMgrRef ! CreateScraperAgent("http://rss.nytimes.com/services/xml/rss/nyt/Science.xml", "nytimes", Tag.SCIENCE, new NytimesParser)
    scrapersMgrRef ! CreateScraperAgent("https://www.washingtontimes.com/rss/headlines/news/politics", "washingtontimes", Tag.POLITICS, new WashingtontimesParser)
    scrapersMgrRef ! CreateScraperAgent("https://www.washingtontimes.com/rss/headlines/culture/technology", "washingtontimes", Tag.TECHNOLOGY, new WashingtontimesParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.reuters.com/Reuters/PoliticsNews", "reuters", Tag.POLITICS, new ReutersParser)
    scrapersMgrRef ! CreateScraperAgent("http://feeds.reuters.com/reuters/scienceNews", "reuters", Tag.SCIENCE, new ReutersParser)
   scrapersMgrRef ! CreateScraperAgent("http://feeds.reuters.com/reuters/technologyNews", "reuters", Tag.TECHNOLOGY, new ReutersParser)
   scrapersMgrRef ! CreateScraperAgent("https://www.theguardian.com/uk/technology/rss", "guardian", Tag.TECHNOLOGY, new GuardianParser)
   scrapersMgrRef ! CreateScraperAgent("https://www.theguardian.com/world/rss", "guardian", Tag.POLITICS, new GuardianParser)

  }

}
