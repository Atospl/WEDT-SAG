package parsers.cnnParser

import enums.{Site, Tag}
import parsers.FeedFetcher


object CnnParser {

  def main(args: Array[String]): Unit = {
    val articlesCNN = FeedFetcher.fetch("http://rss.cnn.com/rss/cnn_allpolitics.rss", Site.CNN, Tag.POLITICS)
    println(articlesCNN)
  }

}
