package agents

import akka.actor.{Actor, ActorLogging, Props}
import dbagent.repository.ArticleRepository
import messages.Messages.SaveArticles

object HttpAgent {
  def props(): Props = Props(new HttpAgent)

}

class HttpAgent extends Actor with ActorLogging {
  override def preStart(): Unit = {
    log.info("HttpAgent started...")
  }


  override def receive: Receive = {
    case SaveArticles(articles) =>
      log.info("Saving articles..")
      articles.foreach(elem => {
        ArticleRepository.save(elem)
      })
  }
}
