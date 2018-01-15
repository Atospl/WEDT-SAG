package parsers.bbcParser

import parsers.Parser
import dbagent.dtos.ArticleDTO
import dbagent.repository.ArticleRepository
import enums.{Site, Tag}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parsers.FeedFetcher

import scala.collection.mutable.ListBuffer


class BbcParser extends Parser {

  def main(args: Array[String]): Unit = {
    parse("http://feeds.bbci.co.uk/news/politics/rss.xml", Tag.POLITICS)
    parse("http://feeds.bbci.co.uk/news/science_and_environment/rss.xml", Tag.SCIENCE)
    parse("http://feeds.bbci.co.uk/news/technology/rss.xml", Tag.TECHNOLOGY)
  }

  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO] = {
    val feed = FeedFetcher.fetch(rssUrl, Site.BBC, tag)
    val documents = new ListBuffer[Document]
    val articles = new ListBuffer[ArticleDTO]
    feed.foreach(elem => {
      val document = Jsoup.connect(elem.url).get()
      val text = document.select(".story-body__inner > p").text()
      if(!text.isEmpty)
        articles += ArticleDTO(elem.dateDownloaded, elem.tags.name, text, None, elem.siteFrom.name, elem.url, elem.title, elem.publishedDate)
    })
    articles
  }

}
