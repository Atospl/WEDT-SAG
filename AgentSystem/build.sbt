name := "AgentSystem"

version := "0.1"

scalaVersion := "2.12.4"
lazy val akkaVersion = "2.5.3"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.github.tminglei" %% "slick-pg" % "0.15.0-RC",
  "io.underscore" %% "slickless" % "0.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "org.scalatest" %% "scalatest" % "3.0.1" % "test",
  "com.rometools" % "rome" % "1.9.0",
  "org.jsoup" % "jsoup" % "1.11.2"
)
