package com.jesperancinha.imagecontour.filters.contour

import java.awt.image.BufferedImage

import com.jesperancinha.imagecontour.filters.ImageFilter

class ImageContour(bgColor: Int, lnColor: Int, diffThreshold: Double, radius: Int, bufferedImage: BufferedImage) extends ImageFilter[BufferedImage] {
  def apply(): BufferedImage = {
    val w: Int = bufferedImage.getWidth
    val h: Int = bufferedImage.getHeight
    val out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB)

    for (i <- 1 until w) {
      for (j <- 1 until h) {
        if (i + radius >= w || j + radius >= h || i <= radius || j <= radius) {
          out.setRGB(i, j, bgColor)
        } else {
          val currentColor: Double = bufferedImage.getRGB(i, j)
          val colorTop: Double = currentColor + diffThreshold
          val colorBottom: Double = currentColor - diffThreshold
          val nextHColor: Double = bufferedImage.getRGB(i + radius, j)
          val nextVColor: Double = bufferedImage.getRGB(i, j + radius)
          val prevHColor: Double = bufferedImage.getRGB(i - radius, j)
          val prevVColor: Double = bufferedImage.getRGB(i, j - radius)
          val drawColor: Int = calculateDrawColor(colorTop, colorBottom, nextHColor, nextVColor, prevHColor, prevVColor)

          out.setRGB(i, j, drawColor)
        }
      }
    }
    out
  }

  private def calculateDrawColor(colorTop: Double, colorBottom: Double, nextHColor: Double, nextVColor: Double, prevHColor: Double, prevVColor: Double) = {
    (colorTop, colorBottom) match {
      case (t, _) if t < nextHColor || t < prevHColor => lnColor
      case (_, b) if b > nextHColor || b > prevHColor => lnColor
      case (t, _) if t < nextVColor || t < prevVColor => lnColor
      case (_, b) if b > nextVColor || b > prevVColor => lnColor
      case (_, _) => bgColor
    }
  }
}

