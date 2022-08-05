package dev.zwander.common.util

import dev.zwander.common.data.DeviceData
import dev.zwander.common.util.externals.GenerateOptions
import dev.zwander.common.util.externals.WriteStreamOptions
import dev.zwander.common.util.externals.jsZip
import dev.zwander.common.util.externals.streamSaver
import io.ktor.client.fetch.*
import kotlinx.browser.window
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual suspend fun triggerDownload(data: List<Pair<String, DeviceData>>, onDone: (Exception?) -> Unit) {
    val zip = window.jsZip()

    data.forEach { (path, data) ->
        zip.file("${path.replace("/CameraDataNode", "")}.json", Json.encodeToString(data))
    }

    zip.generateAsync(
        GenerateOptions().apply {
            type = "uint8array"
        }
    ).then<Unit> {
        val array = it.unsafeCast<Uint8Array>()
        val stream = window.streamSaver().createWriteStream(
            "CameraXData.zip",
            WriteStreamOptions().apply {
                size = array.length.toInt()
            }
        )
        val writer = stream.getWriter()

        writer.write(array)
        writer.close()
    }
}
