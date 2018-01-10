name := "AgentSystem"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.2.0",
  "com.chuusai" %% "shapeless" % "2.3.2",
  "com.github.tminglei" %% "slick-pg" % "0.15.0-RC",
  "io.underscore" %% "slickless" % "0.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
  //"org.postgresql" % "postgresql" % "9.4-1206-jdbc4",
)
