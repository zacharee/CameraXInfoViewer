package dev.zwander.common.util

import dev.zwander.common.data.DeviceData

actual suspend fun triggerDownload(data: List<Pair<String, DeviceData>>, onDone: (Exception?) -> Unit) {

}
