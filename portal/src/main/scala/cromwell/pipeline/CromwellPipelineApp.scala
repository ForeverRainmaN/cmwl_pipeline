package cromwell.pipeline

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.softwaremill.macwire._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.concurrent.ExecutionContextExecutor

object CromwellPipelineApp extends App {

  implicit val system: ActorSystem = ActorSystem("cromwell-pipeline")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val log = LoggerFactory.getLogger(CromwellPipelineApp.getClass)
  val config = ConfigFactory.load()
  val components = wire[ApplicationComponents]

  import components.applicationConfig.webServiceConfig
  import components.controllerModule._
  import components.datastorageModule._
  import components.utilsModule._

  pipelineDatabaseEngine.updateSchema()

  val route = authController.route ~ securityDirective.authenticated { _ =>
    complete(StatusCodes.OK)
  }

  log.info(s"Server online at http://${webServiceConfig.interface}:${webServiceConfig.port}/")
  Http().bindAndHandle(route, webServiceConfig.interface, webServiceConfig.port)

}