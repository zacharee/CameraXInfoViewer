package dev.zwander.common.util.externals

import org.w3c.dom.Window

inline fun Window.jsZip(): JSZip = window.asDynamic().JSZip()
inline fun Window.streamSaver(): StreamSaver = window.asDynamic().streamSaver

fun GenerateOptions(): GenerateOptions = js("{}")
fun WriteStreamOptions(): WriteStreamOptions = js("{}")
