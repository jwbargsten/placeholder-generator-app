package org.bargsten.image

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentType, HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import scala.util.{Failure, Success}

object PlaceholderApp {
  private def startHttpServer(routes: Route)(implicit system: ActorSystem[_]): Unit = {
    import system.executionContext

    val futureBinding = Http().newServerAt("localhost", 8080).bind(routes)
    futureBinding.onComplete {
      case Success(binding) =>
        val address = binding.localAddress
        system.log.info("Server online at http://{}:{}/", address.getHostString, address.getPort)
      case Failure(ex) =>
        system.log.error("Failed to bind HTTP endpoint, terminating system", ex)
        system.terminate()
    }
  }

  def main(args: Array[String]): Unit = {
    val rootBehavior = Behaviors.setup[Nothing] { ctx =>
      val routes: Route = pathPrefix(IntNumber / IntNumber) { (w, h) =>
        get {
          if (w < 10000 && h < 10000) {
            complete {
              val img = new Placeholder(w, h).create
              val out = new ByteArrayOutputStream()
              ImageIO.write(img, "png", out)

              HttpEntity(ContentType(MediaTypes.`image/png`), out.toByteArray)
            }
          } else {
            complete(StatusCodes.BadRequest, HttpEntity("requested dimensions invalid"))
          }
        }
      }
      startHttpServer(routes)(ctx.system)

      Behaviors.empty
    }
    ActorSystem[Nothing](rootBehavior, "PlaceholderHttpServer")
  }
}
