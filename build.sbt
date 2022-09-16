ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "vehicle-state"
  )

val AkkaVersion = "2.6.20"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion