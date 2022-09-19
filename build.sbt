ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

lazy val root = (project in file("."))
  .settings(
    name := "vehicle-state"
  )

val AkkaVersion = "2.6.20"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion

val AkkaHttpVersion = "10.2.10"
libraryDependencies += "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion

val slickVersion =  "3.4.1"
val slf4jVersion = "1.7.26"
val hikaricpVersion = "3.4.0"
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % slickVersion,
  "org.slf4j" % "slf4j-nop" % slf4jVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % hikaricpVersion
)

val h2Version = "2.1.214"
libraryDependencies += "com.h2database" % "h2" % h2Version


val scalatestVersion = "3.2.13"
libraryDependencies += "org.scalactic" %% "scalactic" % scalatestVersion
libraryDependencies += "org.scalatest" %% "scalatest" % scalatestVersion % "test"
