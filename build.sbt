name := """scalaAppServices"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "net.codingwell" %% "scala-guice" % "4.0.1",
  specs2 % Test,
  "org.scalatest" % "scalatest_2.11" % "2.2.6",
  "org.mockito" % "mockito-all" % "1.10.19"
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

routesGenerator := InjectedRoutesGenerator
