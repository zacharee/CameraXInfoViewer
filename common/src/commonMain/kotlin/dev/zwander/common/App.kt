package dev.zwander.common

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.zwander.common.ui.MainContent
import dev.zwander.common.util.initializeApp

@Composable
fun MainApp() {
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(null) {
        initializeApp()
        loading = false
    }

    MaterialTheme(
        colors = darkColors()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Crossfade(
                modifier = Modifier.fillMaxSize(),
                targetState = loading
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (it) {
                        CircularProgressIndicator()
                    } else {
                        MainContent(
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }
    }
}
