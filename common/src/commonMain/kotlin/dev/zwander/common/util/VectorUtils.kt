package dev.zwander.common.util

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import dev.icerock.moko.resources.AssetResource

@Composable
fun vectorResource(resource: AssetResource): Painter {
    var painter by remember(resource) { mutableStateOf<Painter?>(null) }

    vectorResourceImpl(resource) {
        painter = it
    }

    return painter ?: ColorPainter(Color.Transparent)
}

@Composable
expect fun vectorResourceImpl(resource: AssetResource, result: (Painter) -> Unit)
