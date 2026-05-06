package org.example.project.ui.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BarChart(
    labels: List<String>,
    values: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = Color(0xFF4A90D9),
    axisColor: Color = Color(0xFF888888)
) {
    Canvas(modifier = modifier.padding(horizontal = 8.dp)) {
        if (values.isEmpty()) return@Canvas

        val padL = 56f; val padR = 16f; val padT = 24f; val padB = 44f
        val chartW = size.width - padL - padR
        val chartH = size.height - padT - padB
        val maxVal = values.max().coerceAtLeast(0.001f)
        val n = values.size
        val slotW = chartW / n
        val barW = slotW * 0.55f

        // Сетка и оси
        drawLine(axisColor, Offset(padL, padT), Offset(padL, padT + chartH), 2f)
        drawLine(axisColor, Offset(padL, padT + chartH), Offset(padL + chartW, padT + chartH), 2f)

        for (step in 0..4) {
            val ratio = step / 4f
            val y = padT + chartH - ratio * chartH
            drawLine(Color(0xFFDDDDDD), Offset(padL, y), Offset(padL + chartW, y), 1f)
            drawChartLabel(padL - 4f, y + 8f, "%.2f".format(ratio * maxVal), centered = false)
        }

        // Столбцы
        values.forEachIndexed { i, v ->
            val cx = padL + i * slotW + slotW / 2f
            val barH = (v / maxVal) * chartH
            val top = padT + chartH - barH

            drawRect(
                color = barColor,
                topLeft = Offset(cx - barW / 2f, top),
                size = Size(barW, barH)
            )
            // Значение над столбцом
            drawChartLabel(cx, top - 6f, "%.3f".format(v), centered = true, textSizePx = 24f)
            // Название под осью
            val lbl = labels.getOrElse(i) { "A${i+1}" }.let {
                if (it.length > 5) it.take(4)+"…" else it
            }
            drawChartLabel(cx, padT + chartH + 28f, lbl, centered = true, textSizePx = 24f)
        }
    }
}