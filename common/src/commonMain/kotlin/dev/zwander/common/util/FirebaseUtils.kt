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
expect suspend fun fetchAllDocuments(): List<DeviceData>

suspend fun List<DeviceData>.sortDocuments(): Map<String, List<DeviceData>> {
    val sorted = HashMap<String, MutableList<DeviceData>>()

    forEach { data ->
        val id = "${data.brand}_${data.model}"

        if (!sorted.containsKey(id)) {
            sorted[id] = mutableListOf()
        }

        sorted[id]?.add(data)
    }

    return sorted
}
