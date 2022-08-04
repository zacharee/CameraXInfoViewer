package dev.zwander.common

import android.app.Application
import android.content.Context

class App : Application() {
    companion object {
        @Volatile
        var appContext: Context? = null
    }

    override fun onCreate() {
        appContext = this

        super.onCreate()
    }
}
