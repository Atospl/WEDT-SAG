package parsers

import java.net.URL
import java.sql.Timestamp
import java.time.LocalDateTime

import com.rometools.rome.feed.synd.SyndFeed
import com.rometools.rome.io.{SyndFeedInput, XmlReader}
import enums.{Site, Tag}
import parsers.dtos.EntryDTO

import scala.collection.mutable.ListBuffer

object FeedFetcher {

  def fetch(url: String, siteFrom: Site, tags: Tag): List[EntryDTO] = {
    val input: SyndFeedInput = new SyndFeedInput()
    val feed: SyndFeed = input.build(new XmlReader(new URL(url)))
    val entries = feed.getEntries
    var articles = new ListBuffer[EntryDTO]
    entries.forEach(elem => {
      if (elem.getPublishedDate != null) {
        articles += EntryDTO(url = elem.getLink, title = elem.getTitle, siteFrom = siteFrom, dateDownloaded = LocalDateTime.now.toString, tags = tags, publishedDate = new Timestamp(elem.getPublishedDate.getTime))
      }
    })
    articles.toList
  }

}
