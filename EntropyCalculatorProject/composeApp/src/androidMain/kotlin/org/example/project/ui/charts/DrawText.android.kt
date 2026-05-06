package org.example.project.ui.charts

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas

actual fun DrawScope.drawChartLabel(
    x: Float, y: Float, text: String, centered: Boolean, textSizePx: Float
) {
    drawContext.canvas.nativeCanvas.apply {
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.rgb(100, 100, 100)
            this.textSize = textSizePx
            if (centered) textAlign = android.graphics.Paint.Align.CENTER
            isAntiAlias = true
        }
        drawText(text, x, y, paint)
    }
}