package com.jesper.imagecontour.filters

import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.nio.file.{Files, Paths}
import javax.imageio.ImageIO

/**
  * Created by joaofilipesabinoesperancinha on 09-03-16.
  * Utility Class
  */
object ImageManager {

  def getBufferedImage(fileName: String): BufferedImage = {
    val fileBytes = Files.readAllBytes(Paths.get(fileName))
    val byteStream: java.io.InputStream = new ByteArrayInputStream(fileBytes)
    val bImageFromConvert: BufferedImage = ImageIO.read(byteStream);
    byteStream.close()
    bImageFromConvert
  }

  def getBufferedImage(byteStream: java.io.InputStream): BufferedImage = {
    val bImageFromConvert: BufferedImage = ImageIO.read(byteStream);
    byteStream.close()
    bImageFromConvert
  }
}
