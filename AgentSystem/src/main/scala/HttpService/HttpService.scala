package HttpService

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import HttpMethods._
import agents.ArtCompSystemApp
import akka.http.scaladsl.unmarshalling.Unmarshal

import scala.concurrent.Future
import scala.util.{Failure, Success}
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object HttpService {

  implicit val system = ArtCompSystemApp.system
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  final case class Vec(vector: List[Double])

  implicit val vectorFormat = jsonFormat1(Vec)

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
