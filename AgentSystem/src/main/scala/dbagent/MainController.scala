package dbagent

import dbagent.dtos.ArticleDTO
import dbagent.repository.ArticleRepository

object MainController {

  def main(args: Array[String]): Unit = {
    println(ArticleRepository.getAll)
    /*println(ArticleRepository.getByTagName("b"))
    println(ArticleRepository.getByTagName("c"))
    ArticleRepository.save(ArticleDTO("a", "a", "a", Some("a"), "a", "a", "a"))
    ArticleRepository.delete(2)*/
  }

}
