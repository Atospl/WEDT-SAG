package parsers

import enums.{Site, Tag}
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import HttpService.ScraperHttpService
import dbagent.dtos.ArticleDTO

import scala.collection.mutable.ListBuffer


class GuardianParser extends Parser {

  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO] = {
    val feed = FeedFetcher.fetch(rssUrl, Site.GUARDIAN, tag)
    val documents = new ListBuffer[Document]
    val articles = new ListBuffer[ArticleDTO]
    feed.foreach(elem => {
      val document = Jsoup.connect(elem.url).get()
      val text = document.select(",content__article-body > p").text()
      if (!text.isEmpty) {
        val vector = ScraperHttpService.getVector(text)
        articles += ArticleDTO(elem.dateDownloaded, elem.tags.name, text, vector, elem.siteFrom.name, elem.url, elem.title, elem.publishedDate)
      }
    })
    articles
  }

}
