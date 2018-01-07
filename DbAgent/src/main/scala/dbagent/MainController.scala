package dbagent

import dbagent.repository.ArticleRepository

object MainController {

  def main(args: Array[String]): Unit = {
    println(ArticleRepository.getAll)
  }

}
