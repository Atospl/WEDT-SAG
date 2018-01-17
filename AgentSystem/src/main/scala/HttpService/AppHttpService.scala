package HttpService

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import agents.ArtCompSystemApp
import akka.actor.ActorSystem
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import dbagent.models.ArticleModel
import spray.json.DefaultJsonProtocol._
import spray.json._
import HttpMethods._
import dtos.SimilarityDTO

import scala.collection.mutable.ListBuffer

object AppHttpService {

  implicit val system = ArtCompSystemApp.system
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  final case class Vec(vector: List[Double])

  final case class SimilarVectors(id: String, vector: Array[Double])

  final case class SimilarVectorsWrapper(query_id: String, vectors: Array[JsValue])

  final case class JsonWrapper(json: JsValue)

  implicit val vectorFormat = jsonFormat1(Vec)
  implicit val similarVectorsFormat = jsonFormat2(SimilarVectors)
  implicit val similarVectorsWrapperFormat = jsonFormat2(SimilarVectorsWrapper)
  implicit val jsonWrapperFormat = jsonFormat1(JsonWrapper)
  implicit val similarityDtoFormat = jsonFormat2(SimilarityDTO)

  def getSimilar(article: ArticleModel, articles: List[ArticleModel]): List[SimilarityDTO] = {
    var similarity = new ListBuffer[SimilarityDTO]
    var vectors = new ListBuffer[JsValue]
    articles.foreach { elem => {
      var list = new ListBuffer[Double]
      elem.vector.split(",").foreach { elem =>
        list += elem.toDouble
      }
      vectors += SimilarVectors(elem.id.toString, list.toArray).toJson
    }
    }

    val parameters = SimilarVectorsWrapper(article.id.toString, vectors.toArray)
    val json = JsonWrapper(parameters.toJson)
    val entityJson = HttpEntity(ContentTypes.`application/json`, json.toJson.toString)
    Await.result({
      Http().singleRequest(HttpRequest(POST, uri = Uri("http://localhost:5000/similar"), entity = entityJson)).map {
        value =>
          Await.result({
            Unmarshal(value).to[List[(String, Double)]].map { results =>
              results.foreach({ elem =>
                similarity += SimilarityDTO(elem._1, elem._2)
              })
            }
          }, Duration.Inf)
      }
    }, Duration.Inf)
    similarity.toList
  }

}
