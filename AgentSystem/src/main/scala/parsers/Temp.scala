package parsers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import enums.{Site, Tag}
//import parsers.Parser
//import parsers.FeedFetcher

object RSSTest extends App {
  val feed = FeedFetcher.fetch("https://www.theguardian.com/uk/technology/rss", Site.BBC, Tag.POLITICS)
  feed.foreach(elem => {
    val document = Jsoup.connect(elem.url).get()
    val text = document.select(".content__article-body > p").text()
    println(text)
    System.exit(0)
  })
}
