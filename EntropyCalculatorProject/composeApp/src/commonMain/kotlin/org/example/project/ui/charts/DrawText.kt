package org.example.project.ui.charts

import androidx.compose.ui.graphics.drawscope.DrawScope

expect fun DrawScope.drawChartLabel(
    x: Float,
    y: Float,
    text: String,
    centered: Boolean = false,
    textSizePx: Float = 28f
)