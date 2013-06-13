name := "akka-demo"      // 项目名称

organization := "com.gezishu.akka"  // 组织名称

version := "0.0.1-SNAPSHOT"  // 版本号

scalaVersion := "2.10.1"   // 使用的Scala版本号

EclipseKeys.withSource := true

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.4"