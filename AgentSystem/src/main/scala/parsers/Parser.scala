package parsers

import enums.{Site, Tag}
import dbagent.dtos.ArticleDTO
import java.io.Serializable
import scala.collection.mutable.ListBuffer

abstract class Parser extends Serializable {
  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO]
}
