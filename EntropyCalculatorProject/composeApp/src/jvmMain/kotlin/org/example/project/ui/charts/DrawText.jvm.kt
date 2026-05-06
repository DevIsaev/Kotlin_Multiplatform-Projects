package org.example.project.ui.charts

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import org.jetbrains.skia.Font
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine

actual fun DrawScope.drawChartLabel(
    x: Float, y: Float, text: String, centered: Boolean, textSizePx: Float
) {
    drawContext.canvas.nativeCanvas.apply {
        val font = Font(null, textSizePx)
        val paint = Paint().apply {
            color = 0xFF888888.toInt()
        }
        val line = TextLine.make(text, font)
        val drawX = if (centered) x - line.width / 2f else x
        drawTextLine(line, drawX, y, paint)
    }
}