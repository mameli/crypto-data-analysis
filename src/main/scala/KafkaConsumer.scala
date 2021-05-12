import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.functions._

object KafkaConsumer {
  val spark: SparkSession = SparkSession.builder()
    .appName("Integrating Kafka")
    .master("local[2]")
    .getOrCreate()

  def readFromKafka(): Unit = {
    val kafkaDF: DataFrame = spark.readStream
      .format("kafka")
      .option("kafka.bootstrap.servers", "localhost:9092")
      .option("subscribe", "testTopic")
      .option("startingOffsets", "earliest")
      .load()

    kafkaDF
      .select(col("topic"), col("offset"), expr("cast(value as string) as actualValue"))
      .writeStream
      .format("console")
      .outputMode("append")
      .start()
      .awaitTermination()
  }

  def main(args: Array[String]): Unit = {
    readFromKafka()
  }
}
