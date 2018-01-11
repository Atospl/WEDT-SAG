package agents

import akka.actor.{ Actor, Props, ActorSystem }
import scala.io.StdIn


/* class UserAgent extends Actor { */
  // override def receive: Receive = {
    // case "createActors" =>
      // val scrapersManager = context.actorOf(Props[ScrapersManagerAgent], "scrapersManager")
      // val dbAgent = context.actorOf(Props[DBAgent], "dbAgent")
      // println("all user agent child created")

  // }


/* } */


object ArtCompSystemApp extends App {
  val system = ActorSystem("ArtCompSystem")
  println("Hello, world!")

  val scrapersManager = system.actorOf(Props[ScrapersManagerAgent], "scrapersManager")
  val dbAgent = system.actorOf(Props[DBAgent], "dbAgent")
  println(s"DBAgent: $dbAgent, ScrapersManager: $scrapersManager");
  scrapersManager ! "createScraperAgents"

  println(">>> Press ENTER to exit <<<")
  try StdIn.readLine()
  finally system.terminate()
}
