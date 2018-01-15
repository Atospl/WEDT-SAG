package dbagent.dtos

import java.sql.Timestamp

case class ArticleDTO
(
  dataDownloaded: String,
  tags: String,
  textOfArticle: String,
  vector: Option[String],
  siteFrom: String,
  url: String,
  title: String,
  publishedDate: Timestamp
)

