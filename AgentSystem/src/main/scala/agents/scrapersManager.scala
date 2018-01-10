package agents

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn

class ScrapersManagerAgent extends Actor {
  override def receive: Receive = {
    case "createParserAgents" =>
      println(s"Parsers created!")

    case "runParsers" =>
      println(s"Parsers running!")

  }
}
