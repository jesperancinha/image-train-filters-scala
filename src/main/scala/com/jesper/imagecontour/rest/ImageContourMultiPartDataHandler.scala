package com.jesper.imagecontour.rest

import java.io.File

import akka.actor.ActorSystem
import akka.event.{LogSource, Logging}
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.stream.scaladsl.FileIO
import com.jesper.imagecontour.Boot
import com.jesper.imagecontour.filters.{ImageContour, ImageKuwahara, ImageManager, ImageSaver}

import scala.concurrent.{ExecutionContextExecutor, Future}

trait ImageContourMultiPartDataHandler {

  implicit val system: ActorSystem

  implicit def executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  implicit val logSource: LogSource[AnyRef] = new LogSource[AnyRef] {
    def genString(o: AnyRef): String = o.getClass.getName

    override def getClazz(o: AnyRef): Class[_] = o.getClass
  }

  def processMultiPartData: Route = path("images" / Rest) {
    rest =>
      (post & entity(as[Multipart.FormData])) { formData =>
        complete {
          val log = Logging(system, this)
          //noinspection UnnecessaryPartialFunction
          val extractedData: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {
            case bodyPart: BodyPart =>
              log.info(s"received ${bodyPart.name} file")
              val tempFile: File = new File(Boot.fileRootSource, bodyPart.filename.orNull)
              val data = bodyPart.entity.dataBytes.runWith(FileIO.toFile(tempFile)).map(_ => bodyPart.name -> bodyPart.getFilename())
              log.info("saved! - " + tempFile)
              val srcBuff = ImageManager.getBufferedImage(tempFile)
              val filter = rest match {
                case "imageContour" => new ImageContour(0xFFFFFF, 0x000000, 800000, 2)
                case "imageKuwahara" => new ImageKuwahara(2,1)
              }
              val out = filter(srcBuff)
              val destinationFile: File = new File(Boot.fileRootDestination, bodyPart.filename.orNull)
              ImageSaver.copyBufferedImage(out, destinationFile)
              log.info("generated! - " + destinationFile)
              data
          }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)
          extractedData.map(data => HttpResponse(StatusCodes.OK, entity = s"Ok. Got $data"))
            .recover {
              case ex: Exception => HttpResponse(StatusCodes.InternalServerError, entity = "Failed!")
            }
        }
      }
  }

  val routes = processMultiPartData
}
