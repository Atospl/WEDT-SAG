package parsers.bbcParser

import enums.{Site, Tag}
import parsers.FeedFetcher


object BbcParser {

  def main(args: Array[String]): Unit = {
    val articlesBBC = FeedFetcher.fetch("http://feeds.bbci.co.uk/news/politics/rss.xml", Site.BBC, Tag.POLITICS)
    println(articlesBBC)
  }

}
