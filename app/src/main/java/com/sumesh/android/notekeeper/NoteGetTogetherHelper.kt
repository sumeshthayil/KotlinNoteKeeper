package com.sumesh.android.notekeeper

import android.content.Context
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class NoteGetTogetherHelper (private val context: Context, val lifecycle: Lifecycle): LifecycleObserver {

    init {
        lifecycle.addObserver(this)
    }
    val tag = this::class.java.simpleName
    var currentLat = 0.0
    var currentLon = 0.0

    val locManager = PseudoLocationManager(context){lat, lon ->
        currentLat = lat
        currentLon = lon
        Log.d(tag, "Location callback Lat:$lat Lon: $lon")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startHandler(){
        locManager.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun stopHandler(){
        locManager.stop()
    }
}