package org.bargsten.image

import Placeholder.{center, fontSizes, selectFont}

import java.awt.image.BufferedImage
import java.awt.{Color, Font, RenderingHints}

class Placeholder(width: Int, height: Int) {

  def text = s"${width}x${height}"
  lazy val imgDims: Dimensions = Dimensions(width, height)

  def create: BufferedImage = {
    val fontWithDimsAndAscent = selectFont(imgDims, fontSizes, text)

    val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = img.createGraphics

    g2d.setColor(Color.DARK_GRAY)
    g2d.fillRect(0, 0, img.getWidth, img.getHeight)

    fontWithDimsAndAscent.foreach { case (font, fontDims, asc) =>
      g2d.setRenderingHint(
        RenderingHints.KEY_ALPHA_INTERPOLATION,
        RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY
      )
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
      g2d.setRenderingHint(
        RenderingHints.KEY_COLOR_RENDERING,
        RenderingHints.VALUE_COLOR_RENDER_QUALITY
      )
      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE)
      g2d.setRenderingHint(
        RenderingHints.KEY_FRACTIONALMETRICS,
        RenderingHints.VALUE_FRACTIONALMETRICS_ON
      )
      g2d.setRenderingHint(
        RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR
      )
      g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
      g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE)

      g2d.setColor(Color.WHITE)
      g2d.setFont(font)
      val pos = center(imgDims, fontDims, asc)
      g2d.drawString(text, pos.x, pos.y)
    }

    g2d.dispose()
    img
  }
}

case class Dimensions(width: Int, height: Int)
case class Position(x: Int, y: Int)

object Placeholder {
  val fontSizes = List(6, 7, 8, 9, 10, 11, 12, 14, 16, 18, 21, 24, 36, 48, 60, 72)

  def selectFont(
                  imgDims: Dimensions,
                  fontSizes: List[Int],
                  text: String
                ): Option[(Font, Dimensions, Int)] = {
    val img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
    val g2d = img.createGraphics
    val res = fontSizes.reverse.view
      .map { size =>
        val font = new Font("SansSerif", Font.PLAIN, size)
        g2d.setFont(font)
        val fm = g2d.getFontMetrics

        (font, Dimensions(fm.stringWidth(text), fm.getHeight), fm.getAscent)
      }
      .find { case (_, fDims, _) =>
        fDims.width < imgDims.width && fDims.height < imgDims.height
      }

    g2d.dispose()
    res
  }

  def center(img: Dimensions, font: Dimensions, fontAscent: Int): Position =
    Position((img.width - font.width) / 2, (img.height - font.height) / 2 + fontAscent)
}
