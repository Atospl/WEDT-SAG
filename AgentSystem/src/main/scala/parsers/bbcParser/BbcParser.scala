package parsers.bbcParser

import parsers.Parser
import enums.{Site, Tag}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import parsers.FeedFetcher
import HttpService.HttpService
import dbagent.dtos.ArticleDTO

import scala.collection.mutable.ListBuffer


class BbcParser extends Parser {

  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO] = {
    val feed = FeedFetcher.fetch(rssUrl, Site.BBC, tag)
    val documents = new ListBuffer[Document]
    val articles = new ListBuffer[ArticleDTO]
    feed.foreach(elem => {
      val document = Jsoup.connect(elem.url).get()
      val text = document.select(".story-body__inner > p").text()
      if (!text.isEmpty) {
        val vector = HttpService.getVector(text)
        articles += ArticleDTO(elem.dateDownloaded, elem.tags.name, text, vector, elem.siteFrom.name, elem.url, elem.title, elem.publishedDate)
      }
    })
    articles
  }

}
