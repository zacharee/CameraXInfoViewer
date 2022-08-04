package dev.zwander.common.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.zwander.common.data.DeviceData
import dev.zwander.common.util.fetchAllDocuments
import dev.zwander.common.util.sortDocuments

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainContent(
    modifier: Modifier = Modifier
) {
    val items = remember {
        mutableStateMapOf<String, List<DeviceData>>()
    }

    LaunchedEffect(null) {
        items.clear()
        items.putAll(fetchAllDocuments().sortDocuments())
    }

    val state = rememberLazyListState()

    println(state.isScrollInProgress)

    Column(
        modifier = modifier
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = state,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Card {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "Brand",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "Model",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "Facing",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "Resolution",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "FOV",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "Camera2",
                            modifier = Modifier.weight(0.5f)
                        )

                        Text(
                            text = "CameraX",
                            modifier = Modifier.weight(0.5f)
                        )
                    }
                }
            }

            items.forEach { (id, datas) ->
                item(key = id) {
                    Card {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val shownData = datas.first()

                            Text(
                                text = shownData.brand ?: "",
                                modifier = Modifier.weight(0.5f),
                            )

                            Text(
                                text = shownData.model ?: "",
                                modifier = Modifier.weight(0.5f)
                            )

                            Column(
                                modifier = Modifier.weight(2.5f),
                            ) {
                                shownData.cameras.values.forEach { cam ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = cam.lensFacing ?: "",
                                            modifier = Modifier.weight(0.5f)
                                        )

                                        Text(
                                            text = cam.fov ?: "",
                                            modifier = Modifier.weight(0.5f)
                                        )

                                        Column(
                                            modifier = Modifier.weight(0.5f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            cam.extensions.filter { (_, support) -> support.camera2 }.forEach { (name, support) ->
                                                Text(
                                                    text = name
                                                )
                                            }
                                        }

                                        Column(
                                            modifier = Modifier.weight(0.5f),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            cam.extensions.filter { (_, support) -> support.camerax }.forEach { (name, support) ->
                                                Text(
                                                    text = name
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
