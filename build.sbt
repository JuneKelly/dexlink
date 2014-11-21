name := """dexlink"""

version := "1.0-SNAPSHOT"

lazy val commonSettings = Seq(
  version := "1.0-SNAPSHOT",
  organization := "net.00null",
  scalaVersion := "2.11.1"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)

lazy val tools = (project in file("./tools")).
  settings(commonSettings: _*).
  settings(
    // your settings here
  )

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  filters,
  "org.mongodb" %% "casbah" % "2.7.2",
  "com.github.nscala-time" %% "nscala-time" % "1.4.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4"
)

libraryDependencies ~= { _ map {
  case m if m.organization == "com.typesafe.play" =>
    m.exclude("commons-logging", "commons-logging").
      exclude("com.typesafe.play", "sbt-link").
      exclude("com.typesafe.play", "build-link")
  case m => m
}}
