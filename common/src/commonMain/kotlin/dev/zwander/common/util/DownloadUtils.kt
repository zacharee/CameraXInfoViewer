package dev.zwander.common.util

import dev.zwander.common.data.DeviceData

expect suspend fun triggerDownload(data: List<Pair<String, DeviceData>>, onDone: (Exception?) -> Unit)
