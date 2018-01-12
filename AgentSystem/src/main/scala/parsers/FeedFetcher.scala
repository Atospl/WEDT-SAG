package parsers

import java.net.URL

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}
import enums.{Site, Tag}
import parsers.dtos.EntryDTO

import scala.collection.mutable.ListBuffer

object FeedFetcher {

  def fetch(url: String, siteFrom: Site, tags: Tag): List[EntryDTO] = {
    val input: SyndFeedInput = new SyndFeedInput()
    val feed: SyndFeed = input.build(new XmlReader(new URL(url)))
    val result = new ListBuffer[String]
    val entries = feed.getEntries
    var articles = new ListBuffer[EntryDTO]
    entries.forEach(elem => {
      articles += EntryDTO(url = url, title = elem.getTitle, siteFrom = siteFrom, dateDownloaded = java.time.LocalDate.now.toString, tags = tags)
    })
    articles.toList
  }

}
