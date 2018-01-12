package parsers

import enums.{Site, Tag}
import dbagent.dtos.ArticleDTO

import scala.collection.mutable.ListBuffer

abstract class Parser {
  def parse(rssUrl: String, tag: Tag): ListBuffer[ArticleDTO]
}
