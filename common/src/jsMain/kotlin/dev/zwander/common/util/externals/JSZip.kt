@file:JsModule("jszip")
@file:JsNonModule

package dev.zwander.common.util.externals

import org.khronos.webgl.Uint8Array
import kotlin.js.Date
import kotlin.js.Promise
import kotlin.js.RegExp
import kotlin.reflect.KClass

external interface CompressionOptions {
    val level: Int?
        get() = definedExternally
}

external interface ZipObjectOptions {
    val compression: String?
        get() = definedExternally
    val compressionOptions: CompressionOptions?
        get() = definedExternally
}

external interface BaseOptions : ZipObjectOptions {
    val comment: String?
        get() = definedExternally
}

external interface BaseFileOptions {
    val base64: Boolean?
        get() = definedExternally
    val optimizedBinaryString: Boolean?
        get() = definedExternally
    val createFolders: Boolean?
        get() = definedExternally
}

external interface FileOptions : BaseOptions, BaseFileOptions {
    val binary: Boolean?
        get() = definedExternally
    val date: Date?
        get() = definedExternally
    val unixPermissions: Int?
        get() = definedExternally
    val dosPermissions: Int?
        get() = definedExternally
    val dir: Boolean?
        get() = definedExternally
}

external interface UpdateMetadata {
    val percent: Double?
        get() = definedExternally
    val currentFile: String?
        get() = definedExternally
}

external interface GenerateOptions : BaseOptions {
    var type: String?
    var mimeType: String?
    var platform: String?
    var encodeFileName: ((fileName: String) -> Uint8Array)?
    var streamFiles: Boolean?
}

external interface LoadAsyncOptions : BaseFileOptions {
    val checkCRC32: Boolean?
        get() = definedExternally
    val decodeFileName: ((fileName: Uint8Array) -> String)?
        get() = definedExternally
}

external class Support {
    val arraybuffer: Boolean
    val uint8array: Boolean
    val blob: Boolean
    val nodebuffer: Boolean
    val nodestream: Boolean
}

external class FilesObject {
    operator fun get(name: String): ZipObject
}

external interface JSZip {
//    companion object {
//        fun loadAsync(data: dynamic, options: LoadAsyncOptions): Promise<ZipObject>
//
//        val support: Support
//        var external: KClass<Promise<*>>
//        val version: String
//    }

    val files: FilesObject
    val comment: String

    fun file(name: String): ZipObject?
    fun file(name: RegExp): Array<ZipObject>
    fun file(name: String, data: Any, options: FileOptions? = definedExternally): JSZip
    fun folder(name: String): JSZip
    fun folder(name: RegExp): Array<ZipObject>
    fun forEach(callback: (relativePath: String, file: ZipObject) -> Unit)
    fun filter(predicate: (relativePath: String, file: ZipObject) -> Boolean): Array<ZipObject>
    fun remove(name: String): JSZip
    fun generateAsync(options: GenerateOptions = definedExternally, onUpdate: ((UpdateMetadata) -> Unit)? = definedExternally): Promise<dynamic>
    fun generateInternalStream(options: GenerateOptions): StreamHelper<dynamic>
    fun loadAsync(data: dynamic, options: LoadAsyncOptions): Promise<ZipObject>
}

external class ZipObject {
    val name: String
    val dir: Boolean
    val date: Date
    val comment: String?
    val unixPermissions: Int
    val dosPermissions: Int
    val options: ZipObjectOptions

    fun async(type: String, onUpdate: ((UpdateMetadata) -> Unit)? = definedExternally): Promise<dynamic>
    fun internalStream(type: String): StreamHelper<dynamic>
}

external class StreamHelper<T> {
    fun on(event: String, callback: (T, UpdateMetadata) -> Unit): StreamHelper<T>
    fun on(event: String, callback: () -> Unit): StreamHelper<T>
    fun on(event: String, callback: (Error) -> Unit): StreamHelper<T>
    fun accumulate(callback: ((UpdateMetadata) -> Unit) = definedExternally): Promise<T>
    fun resume(): StreamHelper<T>
    fun pause(): StreamHelper<T>
}
