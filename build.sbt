name := "crypto-data-analysis"

version := "0.1"

scalaVersion := "2.12.10"

val sparkVersion = "3.0.2"
val akkaVersion = "2.5.24"
val akkaHttpVersion = "10.1.7"
val kafkaVersion = "2.4.0"
val JacksonVersion = "2.10.5.1"
val log4jVersion = "2.4.1"

resolvers ++= Seq(
  "bintray-spark-packages" at "https://dl.bintray.com/spark-packages/maven",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com"
)


libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,

  // streaming
  "org.apache.spark" %% "spark-streaming" % sparkVersion,

  // streaming-kafka
  "org.apache.spark" % "spark-sql-kafka-0-10_2.12" % sparkVersion,

  // low-level integrations
  "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-kinesis-asl" % sparkVersion,

  // kafka
  "org.apache.kafka" %% "kafka" % kafkaVersion,
  "org.apache.kafka" % "kafka-streams" % kafkaVersion,

  "org.apache.logging.log4j" % "log4j-api" % log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % log4jVersion,

  "com.softwaremill.sttp.client3" %% "core" % "3.0.0",
  "com.softwaremill.sttp.client3" %% "akka-http-backend" % "3.0.0",
  "com.typesafe.akka" %% "akka-stream" % "2.6.10",
  "com.typesafe.akka" %% "akka-stream-kafka" % "2.0.7",
  "com.fasterxml.jackson.core" % "jackson-databind" % JacksonVersion
)