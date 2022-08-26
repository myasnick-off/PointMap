package com.example.pointmap

import android.app.Application
import com.example.pointmap.di.mainModule
import com.yandex.mapkit.MapKitFactory
import org.koin.core.context.startKoin

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initMap()
    }

    private fun initKoin() {
        startKoin {
            modules(mainModule)
        }
    }

    private fun initMap() {
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
    }

    companion object {
        private const val MAPKIT_API_KEY = "4c87ef51-1364-433e-862b-83a014d31ac4"
    }
}