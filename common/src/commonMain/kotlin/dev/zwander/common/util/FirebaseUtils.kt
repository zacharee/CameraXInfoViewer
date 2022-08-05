package dev.zwander.common.util

import dev.zwander.common.data.DeviceData

val firebaseOptions = hashMapOf(
    "apiKey" to "AIzaSyDf_NOVDMCkwQ2ZMpxucqu3iGFAef0tsCc",
    "authDomain" to "camerax-info.firebaseapp.com",
    "projectId" to "camerax-info",
    "storageBucket" to "camerax-info.appspot.com",
    "messagingSenderId" to "339219575092",
    "applicationId" to "1:339219575092:web:16546c45c4f5355a5ab561"
)

expect suspend fun initializeApp()
expect suspend fun fetchAllDocuments(): List<Pair<String, DeviceData>>

suspend fun List<Pair<String, DeviceData>>.sortDocuments(): Map<String, List<Pair<String, DeviceData>>> {
    val sorted = LinkedHashMap<String, MutableList<Pair<String, DeviceData>>>()

    forEach { (path, data) ->
        val id = "${data.brand}_${data.model}"

        if (!sorted.containsKey(id)) {
            sorted[id] = mutableListOf()
        }

        sorted[id]?.add(path to data)
    }

    return sorted
}
