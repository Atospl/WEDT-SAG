package dbagent.models

import java.sql.Timestamp

case class ArticleModel
(
  id: Int,
  dataDownloaded: String,
  tags: String,
  textOfArticle: String,
  vector: Option[String],
  siteFrom: String,
  url: String,
  title: String,
  publishedDate: Timestamp
)

