package HttpService

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import agents.ScrapersManager
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

object ScraperHttpService {

  implicit val system = ScrapersManager.system
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

  def getVector(text: String): String = {
    val parameters = Map("text" -> text)
    var vector = ""
    Await.result({
      Http().singleRequest(HttpRequest(uri = Uri("http://localhost:5000/vector").withQuery(Query(parameters)))).map {
        value =>
          var resp = None: Option[Vec]
          Await.result({
            Unmarshal(value).to[Vec].map { json =>
              vector = json.vector.mkString(",")
            }
          }, Duration.Inf)
      }
    }, Duration.Inf)
    vector
  }

}
