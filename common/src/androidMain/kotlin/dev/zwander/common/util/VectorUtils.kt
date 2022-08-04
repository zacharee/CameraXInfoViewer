@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "EXPOSED_PARAMETER_TYPE")

package dev.zwander.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.DrawCache
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import com.caverock.androidsvg.PreserveAspectRatio
import com.caverock.androidsvg.SVG
import dev.icerock.moko.resources.AssetResource
import kotlin.math.ceil

@Composable
actual fun vectorResourceImpl(resource: AssetResource, result: (Painter) -> Unit) {
    val context = LocalContext.current
    val density = LocalDensity.current

    result(loadSvgPainter(context.assets.open(resource.path).readBytes(), density))
}

fun loadSvgPainter(
    input: ByteArray,
    density: Density
): Painter {
    val svg = SVG.getFromString(input.decodeToString())

    return SVGPainter(svg, density)
}

private class SVGPainter(
    private val svg: SVG,
    private val density: Density
) : Painter() {
    override val intrinsicSize: Size
        get() = Size(svg.documentWidth, svg.documentHeight) * density.density

    private var previousDrawSize: Size = Size.Unspecified
    private var alpha: Float = 1.0f
    private var colorFilter: ColorFilter? = null

    private val drawCache = DrawCache()

    override fun applyAlpha(alpha: Float): Boolean {
        this.alpha = alpha
        return true
    }

    override fun applyColorFilter(colorFilter: ColorFilter?): Boolean {
        this.colorFilter = colorFilter
        return true
    }

    override fun DrawScope.onDraw() {
        if (previousDrawSize != size) {
            drawCache.drawCachedImage(
                IntSize(ceil(size.width).toInt(), ceil(size.height).toInt()),
                density = this,
                layoutDirection,
            ) {
                drawSvg(size)
            }
        }

        drawCache.drawInto(this, alpha, colorFilter)
    }

    private fun DrawScope.drawSvg(size: Size) {
        drawIntoCanvas {
            svg.documentWidth = size.width
            svg.documentHeight = size.height
            svg.documentPreserveAspectRatio = PreserveAspectRatio.STRETCH
            svg.renderToCanvas(it.nativeCanvas)
        }
    }
}
