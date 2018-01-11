package dbagent.dtos

case class ArticleDTO
(
  dataDownloaded: String,
  tags: String,
  textOfArticle: String,
  vector: String,
  siteFrom: String,
  url: String,
  title: String
)

