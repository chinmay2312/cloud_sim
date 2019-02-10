name := "Chinmay_Gangal_hw1"

version := "0.1"

scalaVersion := "2.12.8"

unmanagedJars in Compile += file("lib/commons-math3-3.6.1.jar")
unmanagedJars in Compile += file("lib/cloudsim-3.0.3.jar")

libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"