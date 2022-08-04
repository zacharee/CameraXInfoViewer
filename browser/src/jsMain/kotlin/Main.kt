@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE", "EXPOSED_PARAMETER_TYPE")

import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.skiko.wasm.onWasmReady
import org.w3c.dom.HTMLCanvasElement
import androidx.compose.ui.native.ComposeLayer
import androidx.compose.ui.window.ComposeWindow
import dev.zwander.common.MainApp
import org.w3c.dom.events.WheelEvent

var canvas = document.getElementById("ComposeTarget") as HTMLCanvasElement

fun canvasResize(width: Int = window.innerWidth, height: Int = window.innerHeight) {
    canvas.setAttribute("width", "$width")
    canvas.setAttribute("height", "$height")
}

fun composableResize(layer: ComposeLayer) {
    val clone = canvas.cloneNode(false) as HTMLCanvasElement
    canvas.replaceWith(clone)
    canvas = clone

    val scale = layer.layer.contentScale
    canvasResize()
    layer.layer.attachTo(clone)
    layer.layer.needRedraw()
    layer.setSize(
        (clone.width / scale).toInt(),
        (clone.height / scale).toInt()
    )
}

fun main() {
    onWasmReady {
        canvasResize()
        ComposeWindow().apply {
            window.addEventListener("resize", {
                composableResize(layer = layer)
            })

            setContent {
                MainApp()
            }
        }
    }
}
