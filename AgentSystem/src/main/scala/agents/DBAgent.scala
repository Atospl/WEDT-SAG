package agents

import messages.Messages._
import dbagent.repository.ArticleRepository

import akka.actor.{Actor, Props, ActorLogging, ActorSystem}
import scala.io.StdIn

object DBAgent {
  def props(): Props = Props(new DBAgent)
}

class DBAgent extends Actor with ActorLogging {
  override def preStart(): Unit = {
    log.info("DBAgent started...")
  }


  override def receive: Receive = {
    case SaveArticles(articles) =>
      log.info("Saving articles..")
      articles.foreach(elem => {
        ArticleRepository.save(elem)
      })

    case SaveArticle(article) =>
      log.info("Saving article..")
      ArticleRepository.save(article)
  }

}
