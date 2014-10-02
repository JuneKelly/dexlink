name := """dexlink"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws,
  "org.mongodb" %% "casbah" % "2.7.2",
  "com.github.nscala-time" %% "nscala-time" % "1.4.0",
  "com.github.t3hnar" %% "scala-bcrypt" % "2.4"
)
