package com.jesper.imagecontour

import java.awt.image.BufferedImage

import com.jesperancinha.imagecontour.filters.chartizate.ImageChartizate
import com.jesperancinha.imagecontour.filters.kuwahara.ImageKuwahara
import org.scalatest.{BeforeAndAfterEach, FunSuite}

class ImageChartizateTest extends FunSuite with BeforeAndAfterEach {

  test("testConvertAndSaveImage") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testKuwahara1.png")
    val imageChartizate = new ImageChartizate(0, 50, 10, "Arial", 5, "LATIN_EXTENDED_A", sourceImage)
    imageChartizate.apply()
  }

  test("testConvertAndSaveImageKuwahara2") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testKuwahara2.png")
    val imageChartizate = new ImageChartizate(0, 50, 10, "Arial", 5, "LATIN_EXTENDED_A", sourceImage)
    imageChartizate.apply()
  }

  test("testConvertAndSaveImageEyePantherBW") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testEyeBW.png")
    val imageChartizate = new ImageChartizate(0, 50, 10, "Arial", 5, "LATIN_EXTENDED_A", sourceImage)
    imageChartizate.apply()
  }

  test("testConvertAndSaveImageEyePanther") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testEye.png")
    val imageChartizate = new ImageChartizate(0, 50, 10, "Arial", 5, "LATIN_EXTENDED_A", sourceImage)
    imageChartizate.apply()
  }

  test("testConvertAndSaveImagePanther") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testPanther.jpg")
    val imageChartizate = new ImageKuwahara(4, 1, sourceImage)
    imageChartizate.apply()
  }

  test("testConvertAndSaveImagePanther5Its") {
    val sourceImage: BufferedImage = ImageTestUtils.getBufferedResource("/testPanther1.jpg")
    val imageChartizate = new ImageKuwahara(2, 5, sourceImage)
    imageChartizate.apply()
  }

}
