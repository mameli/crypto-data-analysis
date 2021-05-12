import akka.Done
import sttp.client3._
import sttp.client3.akkahttp.AkkaHttpBackend
import sttp.ws.WebSocket

import scala.concurrent.ExecutionContext.Implicits.global
import akka.actor.{ActorSystem, Cancellable}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.scaladsl.Source
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future
import scala.concurrent.duration._

object WebSocketAkka extends App {
  def useWebSocket(ws: WebSocket[Future]): Future[Done] = {
    def send(s: String) = ws.sendText(s)

    //    def receive() = ws.receiveText().map(t => println(s"RECEIVED: $t"))

    implicit val system: ActorSystem = ActorSystem()

    val ws_source: Source[Future[String], Cancellable] = Source
      .tick(1.second, 0.1.second, ())
      .map(_ => ws.receiveText())

    val config = system.settings.config.getConfig("akka.kafka.producer")
    val producerSettings =
      ProducerSettings(config, new StringSerializer, new StringSerializer)
        .withBootstrapServers("localhost:9092")

    val kafka_source = ws_source
      .mapAsync(1)(m => m)
      .map(msg => {
        system.log.info(s"producing $msg")
        new ProducerRecord[String, String]("test", msg)
      })
      .log("producer")

    send(
      """
        |{
        |    "event": "bts:subscribe",
        |    "data": {
        |        "channel": "live_orders_btcusd"
        |    }
        |}
        |""".stripMargin)
    kafka_source.runWith(Producer.plainSink(producerSettings))
  }

  val backend = AkkaHttpBackend()

  basicRequest
    .response(asWebSocket(useWebSocket))
    .get(uri"wss://ws.bitstamp.net")
    .send(backend)
    .onComplete(_ => backend.close())
}