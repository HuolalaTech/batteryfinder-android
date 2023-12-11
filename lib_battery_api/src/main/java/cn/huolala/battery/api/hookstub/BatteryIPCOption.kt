package cn.huolala.battery.api.hookstub

import android.content.Context
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.location.ClientMessenger

@Keep
object BatteryIPCOption {
    fun openLocationIPC(context: Context) {
        ClientMessenger.bindServiceInvoked(context)
    }
}