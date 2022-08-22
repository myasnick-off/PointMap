package com.example.pointmap

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }

    companion object {
        private const val MAPKIT_API_KEY = "4c87ef51-1364-433e-862b-83a014d31ac4"
    }
}