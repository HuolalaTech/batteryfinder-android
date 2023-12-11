package cn.huolala.battery.api.hookstub.batterypower

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class BatteryBroadCastReceiver(private val receive: (time: Long, action: String) -> Unit) :
    BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        synchronized(this) {
            intent?.action?.let { receive.invoke(SystemClock.elapsedRealtime(), it) }
        }
    }
}