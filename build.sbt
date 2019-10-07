name := """playtodo"""
organization := "playtodo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.0"

val playPac4jVersion = "4.0.0"
val pac4jVersion = "2.1.0"
val playVersion = "2.6.6"

libraryDependencies ++= Seq(
  guice,
  ehcache,
  "org.pac4j" % "play-pac4j" % playPac4jVersion,
  "org.pac4j" % "pac4j-oidc" % pac4jVersion exclude("commons-io" , "commons-io"),
)
//libraryDependencies += "be.objectify" %% "deadbolt-java" % "2.6.1"
//libraryDependencies += "org.pac4j" %% "play-pac4j" % "7.0.1"
