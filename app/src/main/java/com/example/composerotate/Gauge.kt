package com.example.composerotate

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin

@Immutable
data class FaceConfig(
    val initialAngle: Int = 225,
    val scaleTextDistance: Dp = 24.dp,
    val maxAngleRotation: Int = 270,
    val scaleConfig: ScaleConfig = ScaleConfig(
        steppingAngle = 30f,
        markSize = 16.dp,
        markWidth = 5f
    ),
    val subscaleConfig: ScaleConfig = ScaleConfig(
        steppingAngle = 3f,
        markSize = 8.dp,
        markWidth = 3f
    ),
    val markHalfSubscale: Boolean = true
)

data class ScaleConfig(
    val steppingAngle: Float,
    val markSize: Dp,
    val markWidth: Float,
)

@OptIn(ExperimentalTextApi::class)
@Composable
fun Gauge(
    size: Dp = 250.dp,
    config: FaceConfig = FaceConfig(
        initialAngle = 220, //To be symmetrical it should be 225 but in GC8 this is bit rotated
        maxAngleRotation = 270
    ),
    measurer: TextMeasurer = rememberTextMeasurer()
) {
    val infiniteTransition = rememberInfiniteTransition()
    val a by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = config.maxAngleRotation.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val angle = Math.toRadians((a.toDouble() + config.initialAngle) % 360).toFloat()

    Canvas(
        modifier = Modifier
            .width(size)
            .height(size)
    ) {
        val center = size.toPx() / 2
        val radius = size.toPx() / 2 - 16.dp.toPx()

        drawGaugeHand(
            center = center,
            radius = radius,
            angle = angle
        )
    }


    Canvas(
        modifier = Modifier
            .width(size)
            .height(size)
    ) {
        val center = size.toPx() / 2
        val radius = size.toPx() / 2 - 16.dp.toPx()

        drawGaugeFace(
            radius = radius,
            center = center,
            measurer = measurer,
            config = config
        )
    }

}

private fun DrawScope.drawGaugeHand(
    center: Float,
    radius: Float,
    angle: Float
) {
    val pointX = (center + radius * sin(angle.toDouble())).toFloat()
    val pointY = (center - radius * cos(angle.toDouble())).toFloat()
    drawLine(
        start = Offset(x = center, y = center),
        end = Offset(x = pointX, y = pointY),
        color = Color.Red,
        strokeWidth = 5F
    )
}

@OptIn(ExperimentalTextApi::class)
private fun DrawScope.drawGaugeFace(
    radius: Float,
    center: Float,
    measurer: TextMeasurer,
    config: FaceConfig
) = with(config) {

    Log.d("anim", "drawing morda")
    val scaleMarkSize = scaleConfig.markSize.toPx()
    val subscaleMarkSize = subscaleConfig.markSize.toPx()
    drawCircle(
        brush = SolidColor(Color.LightGray),
        radius = radius,
        style = Stroke(15f)
    )
    val textSize = IntSize(
        //TODO in 1.3.0-alpha 2 this seems to be bugged and not really draws text in center
        // Change it to respect SP instead of dps
        width = 7.dp.toPx().toInt(),
        height = 16.dp.toPx().toInt()
    )
    var i = 0f
    while (i <= maxAngleRotation) {
        val angle = Math.toRadians(i.toDouble() + initialAngle)
        val startX = (center + radius * sin(angle)).toFloat()
        val startY = (center - radius * cos(angle)).toFloat()

        val shouldDrawScaleMark = i % scaleConfig.steppingAngle == 0f
        if (shouldDrawScaleMark){
            val stopX = (center + (radius - scaleMarkSize) * sin(angle)).toFloat()
            val stopY = (center - (radius - scaleMarkSize) * cos(angle)).toFloat()

            val textX = (center + (radius - scaleTextDistance.toPx()) * sin(angle)).toFloat()
            val textY = (center - (radius - scaleTextDistance.toPx()) * cos(angle)).toFloat()
            val text = (i/scaleConfig.steppingAngle).toString()
            drawText(
                measurer,
                text = text,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 12.sp
                ),
                topLeft = Offset(
                    x = textX - textSize.width / 2,
                    y = textY - textSize.height / 2
                ),
                size = textSize
            )

            drawLine(
                start = Offset(startX, startY),
                end = Offset(stopX, stopY),
                color = Color.Blue,
                strokeWidth = scaleConfig.markWidth
            )
        } else{
            val halfSubscaleAngle = scaleConfig.steppingAngle / 2f
            val shouldDrawHalfSubscale = markHalfSubscale && (i % scaleConfig.steppingAngle) == halfSubscaleAngle
            val markSize: Float
            val strokeWidth: Float
            if (shouldDrawHalfSubscale){
                markSize = (scaleMarkSize + subscaleMarkSize) / 2
                strokeWidth = scaleConfig.markWidth
            } else {
                markSize = subscaleMarkSize
                strokeWidth = subscaleConfig.markWidth
            }
            val stopX = (center + (radius - markSize) * sin(angle)).toFloat()
            val stopY = (center - (radius - markSize) * cos(angle)).toFloat()
            drawLine(
                start = Offset(startX, startY),
                end = Offset(stopX, stopY),
                color = Color.Blue,
                strokeWidth = strokeWidth
            )
        }
        i += subscaleConfig.steppingAngle
    }
}

@OptIn(ExperimentalTextApi::class)
@Preview
@Composable
fun PreviewGauge() {
    Gauge()
}