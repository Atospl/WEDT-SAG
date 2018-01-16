package dbagent.repository

import dbagent.dtos.ArticleDTO
import dbagent.models.ArticleModel
import slick.jdbc.PostgresProfile.api._
import java.sql.Timestamp

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration.Duration

object ArticleRepository {

  private val db = Database.forConfig("postgresConf")

  var article = TableQuery[ArticleTableRepository]

  class ArticleTableRepository(tag: Tag) extends Table[ArticleModel](tag, "Article") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def dataDownloaded = column[String]("data_downloaded")

    def tags = column[String]("tags")

    def textOfArticle = column[String]("text_of_article")

    def vector = column[String]("vector")

    def siteFrom = column[String]("site_from")

    def url = column[String]("url")

    def title = column[String]("title")

    def publishedDate = column[Timestamp]("published_date")

    def * = (id, dataDownloaded, tags, textOfArticle, vector, siteFrom, url, title, publishedDate) <> (ArticleModel.tupled, ArticleModel.unapply)

  }

  def getAll: List[ArticleModel] = {
    Await.result({
      db.run {
        article.to[List].result
      }
    }, Duration.Inf)
  }

  def save(dataDownloaded: String, tags: String, textOfArticle: String, vector: String, siteFrom: String, url: String, title: String, publishedDate: Timestamp) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "Article"("data_downloaded","tags","text_of_article","vector","site_from","url","title", "published_date")
                VALUES (${dataDownloaded},${tags},${textOfArticle}, ${vector}, ${siteFrom}, ${url}, ${title}, ${publishedDate})""")
    }, Duration.Inf)
  }

  def save(model: ArticleDTO) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "Article"("data_downloaded","tags","text_of_article","vector","site_from","url","title", "published_date")
                VALUES (${model.dataDownloaded},${model.tags},${model.textOfArticle}, ${model.vector}, ${model.siteFrom}, ${model.url}, ${model.title}, ${model.publishedDate})""")
    }, Duration.Inf)
  }

  def delete(id: Int) = {
    Await.result({
      db.run(
        sqlu"""DELETE FROM "Article" WHERE "id" = ${id}""")
    }, Duration.Inf)
  }

  def getByTagName(tag: String): Option[List[ArticleModel]] = {
    var articles = None: Option[List[ArticleModel]]
    Await.result({
      db.run {
        article.filter(_.tags === tag).to[List].result
      }.map { value =>
        if (value.nonEmpty) {
          articles = Some(value)
        }
      }
    }, Duration.Inf)
    articles
  }

  def updateVectorById(id: Int, vector: String): Unit = {
    Await.result({
      db.run(
        sqlu"""UPDATE "Article" SET "vector" = ${vector} WHERE "id" = ${id}""")
    }, Duration.Inf)
  }

}
