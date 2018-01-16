package messages

import enums.{Site, Tag}
import parsers.Parser
import dbagent.dtos.ArticleDTO

import scala.collection.mutable.ListBuffer

object Messages {
  /** Creates scraper responsible for reading RSS channel from given url */
  final case class CreateScraperAgent(url: String, name: String, tag: Tag, parserObj: Parser)
  
  /** Orders ScrapersManager to begin process of scraping in every underlying actor */
  final case class OrderScrapping()

  /** Orders scrapers to parse and save results into db */
  final case class Scrap()

  /** Send to DBAgent, orders saving articles into database */
  final case class SaveArticles(articles: ListBuffer[ArticleDTO])

  /** Send to DBAgent, orders saving article into database */
  final case class SaveArticle(article: ArticleDTO)

}
