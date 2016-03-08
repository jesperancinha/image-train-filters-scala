package com.steelzack.imagecontour

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, File}
import java.nio.file.{Files, Paths}
import javax.imageio.ImageIO

/**
  * Created by joaofilipesabinoesperancinha on 03-03-16.
  */
object ImageContour extends App {

  override def main(args: Array[String]) {
    val fileName: String = args(0)
    val fileBytes = Files.readAllBytes(Paths.get(fileName))
    val byteStream: java.io.InputStream = new ByteArrayInputStream(fileBytes)
    val bImageFromConvert: BufferedImage = ImageIO.read(byteStream);
    byteStream.close
    convertAndSaveImage(bImageFromConvert, 0x0000, 0xFFFF, 0.20)
  }


  def convertAndSaveImage(source: BufferedImage, //
                          bgColor: Int, //
                          lnColor: Int, //
                          diffThreshold: Double //
                         ) {
    val w: Int = source.getWidth
    val h: Int = source.getHeight
    val arr = Array.fill[Double](3)(0.0)
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    var i = 0
    var j = 0
    for (i <- 1 to w - 1) {
      for (j <- 1 to h - 1) {
        if (i + 1 == w || j + 1 == h) {
          out.setRGB(i, j, source.getRGB(i, j))
        } else {
          //source.getData().getPixel(i, j, arr)
          //print("(" + i + "," + j + ")")
          val currentColor: Double = source.getRGB(i, j)
          val diff: Double = diffThreshold * currentColor
          val colorTop: Double = currentColor + diff
          val colorBottom: Double = currentColor - diff
          val nextHColor: Double = source.getRGB(i + 1, j)
          val nextVColor: Double = source.getRGB(i, j + 1)
          var drawColor = bgColor;

          if (colorTop > nextHColor) {
            drawColor = lnColor;
          } else if (colorBottom < nextHColor) {
            drawColor = lnColor
          } else if (colorTop > nextVColor) {
            drawColor = lnColor
          } else if (colorBottom < nextVColor) {
            drawColor = lnColor
          }

          out.setRGB(i, j, drawColor)
        }
      }
    }

    ImageIO.write(out, "jpg", new File("/tmp/copy.jpg"))
  }

  def apply(): Unit = {

  }
}
