package messages

import parsers.Parser

object Messages {
  /** Creates scraper responsible for reading RSS channel from given url*/
  final case class CreateScraperAgent(url: String, name: String, tag: String, parserObj: Parser)
  






}
