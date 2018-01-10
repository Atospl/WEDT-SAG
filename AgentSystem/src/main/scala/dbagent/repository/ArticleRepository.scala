package dbagent.repository

import dbagent.models.ArticleModel
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent._
import scala.concurrent.duration.Duration

object ArticleRepository {

  private val db = Database.forConfig("postgresConf")

  var field = TableQuery[ArticleTableRepository]

  class ArticleTableRepository(tag: Tag) extends Table[ArticleModel](tag, "Article") {

    def id = column[Int]("id", O.PrimaryKey)

    def dataDownloaded = column[String]("data_downloaded")

    def tags = column[String]("tags")

    def textOfArticle = column[String]("text_of_article")

    def vector = column[String]("vector")

    def siteFrom = column[String]("site_from")

    def url = column[String]("url")

    def title = column[String]("title")

    def * = (id, dataDownloaded, tags, textOfArticle, vector, siteFrom, url, title) <> (ArticleModel.tupled, ArticleModel.unapply)

  }

  def getAll: List[ArticleModel] = {
    Await.result({
      db.run {
        field.to[List].result
      }
    }, Duration.Inf)
  }

/*  def save(a: String, b: String, c: String) = {
    Await.result({
      db.run(
        sqlu"""INSERT INTO "Article"("data_downloaded","tags","text_of_article","vector","site_from","url","title")
              VALUES (${a},${b},${c})""")
    }, Duration.Inf)
  }*/

}
