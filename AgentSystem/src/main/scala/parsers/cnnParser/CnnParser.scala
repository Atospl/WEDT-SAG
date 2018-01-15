package parsers.cnnParser

import parsers.Parser
import dbagent.dtos.ArticleDTO
import enums.{Site, Tag}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parsers.FeedFetcher

import scala.collection.mutable.ListBuffer


class CnnParser extends Parser {

  def main(args: Array[String]): Unit = {
    parse("http://rss.cnn.com/rss/cnn_allpolitics.rss", Tag.POLITICS)
    parse("http://rss.cnn.com/rss/cnn_tech.rss", Tag.TECHNOLOGY)
  }

  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO] = {
    val feed = FeedFetcher.fetch(rssUrl, Site.CNN, tag)
    val documents = new ListBuffer[Document]
    val articles = new ListBuffer[ArticleDTO]
    feed.foreach(elem => {
      val document = Jsoup.connect(elem.url).get()
      val text = document.select(".zn-body__paragraph").text()
      if(!text.isEmpty)
        articles += ArticleDTO(elem.dateDownloaded, elem.tags.name, text, None, elem.siteFrom.name, elem.url, elem.title, elem.publishedDate)
    })
    articles
  }

}
