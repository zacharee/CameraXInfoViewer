package dev.zwander.common.util

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.zwander.common.App
import dev.zwander.common.data.DeviceData
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

actual suspend fun initializeApp() {
    FirebaseApp.initializeApp(
        App.appContext!!,
        firebaseOptions.run {
            FirebaseOptions.Builder()
                .setApiKey(this["apiKey"]!!)
                .setProjectId(this["projectId"]!!)
                .setStorageBucket(this["storageBucket"]!!)
                .setGcmSenderId(this["gcmSenderId"]!!)
                .setApplicationId(this["applicationId"]!!)
                .build()
        }
    )
    Firebase.auth.signInAnonymously().await()
}

actual suspend fun fetchAllDocuments(): List<Pair<String, DeviceData>> {
    val c = Firebase.firestore.collectionGroup("CameraDataNode")
    val h = c.addSnapshotListener { _, _ -> }
    val r = c.get().await()
    h.remove()

    return r.documents.map {
        it.reference.path to Json.decodeFromString(
            it.data?.values?.last().toString()
        )
    }
}
