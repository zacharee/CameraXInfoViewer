@file:JsModule("streamsaver")
@file:JsNonModule

package dev.zwander.common.util.externals

import io.ktor.client.fetch.*

external interface Strategy {
    var highWaterMark: Int?
    var size: ((chunk: Uint8Array) -> Int)?
}

external interface WriteStreamOptions {
    var size: Int?
    var writableStrategy: Strategy?
    var readableStrategy: Strategy?
}

external class StreamSaver {
    fun createWriteStream(name: String, options: WriteStreamOptions? = definedExternally): WritableStream<Uint8Array>
}
