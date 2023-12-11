package cn.huolala.battery.api.hookstub.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class ApplicationLife : LifecycleEventObserver {
    companion object {
        var createAtForeground = false
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {

            Lifecycle.Event.ON_START -> {
                createAtForeground = true

            }

            Lifecycle.Event.ON_STOP -> {
                createAtForeground = false


            }

            else -> {

            }
        }
    }
}