package org.example.project.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun LineChart(
    points: List<Pair<Float, Float>>,  // (x, y)
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFFE84C4C),
    fillColor: Color = Color(0x33E84C4C),
    axisColor: Color = Color(0xFF888888)
) {
    Canvas(modifier = modifier.padding(horizontal = 8.dp)) {
        if (points.size < 2) return@Canvas

        val padL = 56f; val padR = 16f; val padT = 24f; val padB = 44f
        val chartW = size.width - padL - padR
        val chartH = size.height - padT - padB

        val maxX = points.maxOf { it.first }
        val maxY = points.maxOf { it.second }.coerceAtLeast(0.001f)

        fun toScreenX(x: Float) = padL + (x / maxX) * chartW
        fun toScreenY(y: Float) = padT + chartH - (y / maxY) * chartH

        // Оси
        drawLine(axisColor, Offset(padL, padT), Offset(padL, padT + chartH), 2f)
        drawLine(axisColor, Offset(padL, padT + chartH), Offset(padL + chartW, padT + chartH), 2f)

        // Сетка Y
        for (step in 0..4) {
            val ratio = step / 4f
            val y = padT + chartH - ratio * chartH
            drawLine(Color(0xFFDDDDDD), Offset(padL, y), Offset(padL + chartW, y), 1f)
            drawChartLabel(padL - 4f, y + 8f, "%.1f".format(ratio * maxY), textSizePx = 24f)
        }

        // Подписи X (несколько штук)
        val xStep = (points.size / 5).coerceAtLeast(1)
        points.filterIndexed { i, _ -> i % xStep == 0 || i == points.lastIndex }.forEach { (x, _) ->
            val sx = toScreenX(x)
            drawChartLabel(sx, padT + chartH + 28f, x.toInt().toString(), centered = true, textSizePx = 24f)
        }

        // Заливка под кривой
        val fillPath = Path().apply {
            moveTo(toScreenX(points.first().first), padT + chartH)
            points.forEach { (x, y) -> lineTo(toScreenX(x), toScreenY(y)) }
            lineTo(toScreenX(points.last().first), padT + chartH)
            close()
        }
        drawPath(fillPath, fillColor)

        // Линия
        val linePath = Path().apply {
            points.forEachIndexed { i, (x, y) ->
                if (i == 0) moveTo(toScreenX(x), toScreenY(y))
                else lineTo(toScreenX(x), toScreenY(y))
            }
        }
        drawPath(linePath, lineColor, style = Stroke(width = 3f))

        // Точки
        points.forEach { (x, y) ->
            drawCircle(lineColor, radius = 5f, center = Offset(toScreenX(x), toScreenY(y)))
        }
    }
}