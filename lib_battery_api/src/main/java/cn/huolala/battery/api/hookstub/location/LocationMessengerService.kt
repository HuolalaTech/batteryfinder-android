package cn.huolala.battery.api.hookstub.location

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.util.Log
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.common.Constant
import cn.huolala.battery.api.hookstub.common.Constant.Companion.IPC_CODE
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

internal class LocationMessengerService : Service() {
    private val locationMessenger = Messenger(ServiceHandler())
    override fun onBind(intent: Intent?): IBinder? {
        return locationMessenger.binder
    }

    // 强制设置为主Looper，避免数据问题
    class ServiceHandler : Handler(Looper.getMainLooper()) {
        private val gson = Gson()
        override fun handleMessage(msg: Message) {
            val msgToClient = Message.obtain(msg)
            when (msg.what) {
                IPC_CODE -> {
                    val value = msg.data.getString("key") ?: return
                    val map = gson.fromJsonx<IPCLocationData>(value)
                    Log.e(Constant.TAG, "message ${map}")
                    BatteryFinderDataCenter.locationRecordMap.putAll(map.map)
                    msgToClient.arg1 = IPC_CODE
                    msgToClient.replyTo.send(msgToClient)
                }

            }
            super.handleMessage(msg)
        }
    }
}

inline fun <reified T> Gson.fromJsonx(json: String) =
    this.fromJson<T>(json, object : TypeToken<T>() {}.type)