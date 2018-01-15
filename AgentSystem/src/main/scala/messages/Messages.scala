package messages

import enums.{Site, Tag}
import parsers.Parser

object Messages {
  /** Creates scraper responsible for reading RSS channel from given url */
  final case class CreateScraperAgent(url: String, name: String, tag: Tag, parserObj: Parser)
  
  /** Orders ScrapersManager to begin process of scraping in every underlying actor */
  final case class OrderScrapping()





}
