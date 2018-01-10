package agents

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn

class ParserAgent extends Actor {
  override def receive: Receive = {
    case "parse" =>
      println(s"Parsing...")


  }
}
