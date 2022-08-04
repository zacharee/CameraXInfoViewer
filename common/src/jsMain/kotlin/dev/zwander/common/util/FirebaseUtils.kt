package dev.zwander.common.util

import com.soywiz.korio.jsObjectToMap
import com.soywiz.korio.toJsObject
import dev.zwander.common.data.DeviceData
import dev.zwander.common.util.externals.*
import kotlinx.coroutines.await
import kotlinx.serialization.decodeFromString

actual suspend fun initializeApp() {
    initializeApp(firebaseOptions.toJsObject())

    signInAnonymously(getAuth()).await()
}

actual suspend fun fetchAllDocuments(): List<DeviceData> {
    val group = getDocs(collectionGroup(getFirestore(), "CameraDataNode")).await()
    val docsList = group.docs

    return docsList.map {
        kotlinx.serialization.json.Json.decodeFromString(
            jsObjectToMap(it.data())["second"].toString()
        )
    }
}
