package dev.zwander.common.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.soywiz.korio.async.launch
import com.soywiz.korio.util.toStringDecimal
import dev.zwander.common.data.DeviceData
import dev.zwander.common.util.fetchAllDocuments
import dev.zwander.common.util.sortDocuments
import dev.zwander.common.util.triggerDownload

const val columnCount = 7
val totalWidth = 1200.dp
val minWidth = 1000.dp

val LocalColumnWidth = compositionLocalOf<Dp> { error("No width specified") }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
    val originalItems = remember {
        mutableStateMapOf<String, List<Pair<String, DeviceData>>>()
    }

    val items = remember(originalItems) {
        mutableStateMapOf<String, List<Pair<String, DeviceData>>>()
    }

    var searchText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(null) {
        originalItems.clear()
        originalItems.putAll(fetchAllDocuments().sortDocuments())
    }

    LaunchedEffect(searchText, originalItems.size) {
        val newItems = originalItems.filter { (id, _) ->
            searchText.isBlank() || id.contains(searchText, true)
        }
        items.clear()
        items.putAll(newItems)
    }

    val state = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val rowState = rememberScrollState()
    val rowDragState = rememberDraggableState { scope.launch { rowState.scrollBy(-it) } }

    Column(
        modifier = Modifier.widthIn(max = totalWidth),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    IconButton(
                        onClick = { searchText = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Search"
                        )
                    }
                }
            )

            IconButton(
                onClick = {
                    scope.launch {
                        triggerDownload(items.values.flatten()) {}
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Download"
                )
            }
        }


        Column(
            modifier = modifier
                .horizontalScroll(rowState)
                .draggable(rowDragState, Orientation.Horizontal)
                .widthIn(min = minWidth, max = totalWidth)
        ) {
            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val columnWidth = maxWidth / columnCount

                CompositionLocalProvider(
                    LocalColumnWidth provides columnWidth
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = state,
                        modifier = Modifier.fillMaxSize()
                            .draggable(
                                rememberDraggableState { scope.launch { state.scrollBy(-it) } },
                                Orientation.Vertical
                            )
                    ) {
                        stickyHeader {
                            Card {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ColumnWidthText(
                                        text = "Brand",
                                    )

                                    ColumnWidthText(
                                        text = "Model",
                                    )

                                    ColumnWidthText(
                                        text = "Facing",
                                    )

                                    ColumnWidthText(
                                        text = "Resolution",
                                    )

                                    ColumnWidthText(
                                        text = "FOV",
                                    )

                                    ColumnWidthText(
                                        text = "Camera2",
                                    )

                                    ColumnWidthText(
                                        text = "CameraX",
                                    )
                                }
                            }
                        }

                        items.entries.sortedBy { it.key.lowercase() }.forEach { (id, datas) ->
                            item(key = id) {
                                Card {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        val shownData = datas.maxBy { it.second.sdk ?: 0 }.second

                                        ColumnWidthText(
                                            text = shownData.brand ?: "",
                                        )

                                        ColumnWidthText(
                                            text = shownData.model ?: "",
                                        )

                                        Column(
                                            modifier = Modifier.width(columnWidth * 5),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            shownData.cameras.values.forEach { cam ->
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    ColumnWidthText(
                                                        text = cam.lensFacing ?: "",
                                                    )

                                                    ColumnWidthText(
                                                        text = cam.resolution?.run {
                                                            split("x").mapNotNull { it.toIntOrNull() }
                                                                .let { (width, height) ->
                                                                    (width * height / 1_000_000.0).toStringDecimal(
                                                                        1,
                                                                        true
                                                                    )
                                                                }
                                                        } ?: "",
                                                    )

                                                    ColumnWidthText(
                                                        text = cam.fov?.replace(",", ".") ?: "",
                                                    )

                                                    Column(
                                                        modifier = Modifier.width(columnWidth),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        cam.extensions.filter { (_, support) -> support.camera2 }
                                                            .forEach { (name, _) ->
                                                                Text(
                                                                    text = name,
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            }
                                                    }

                                                    Column(
                                                        modifier = Modifier.width(columnWidth),
                                                        horizontalAlignment = Alignment.CenterHorizontally,
                                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        cam.extensions.filter { (_, support) -> support.camerax }
                                                            .forEach { (name, _) ->
                                                                Text(
                                                                    text = name,
                                                                    textAlign = TextAlign.Center
                                                                )
                                                            }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ColumnWidthText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier.width(LocalColumnWidth.current),
        textAlign = TextAlign.Center
    )
}
