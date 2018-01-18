package parsers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import enums.{Site, Tag}
//import parsers.Parser
//import parsers.FeedFetcher

object RSSTest extends App {
  val feed = FeedFetcher.fetch("https://www.washingtontimes.com/rss/headlines/news/politics", Site.BBC, Tag.POLITICS)
  feed.foreach(elem => {
    val document = Jsoup.connect(elem.url).get()
    val text = document.select(".bigtext > p").text()
    println(text)
    System.exit(0)
  })
}
