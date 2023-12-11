package cn.huolala.battery.api.hookstub.location

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Messenger
import android.os.Process
import android.util.Log
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.common.Constant
import cn.huolala.battery.api.hookstub.common.MainProcessUtil
import com.google.gson.Gson


@Keep
object ClientMessenger {
    var serviceMessenger: Messenger? = null

    @Volatile
    var isConn = false
    private val gson = Gson()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceMessenger = Messenger(service)
            isConn = true
            Log.e(Constant.TAG, "onServiceConnected")

        }

        override fun onServiceDisconnected(name: ComponentName?) {
            serviceMessenger = null
            isConn = false
            Log.e(Constant.TAG, "onServiceDisconnected")
        }

    }


    class ClientHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                Constant.IPC_CODE -> {
                    Log.e(Constant.TAG, "client message ${msg.arg1}")
                }
            }
            super.handleMessage(msg)
        }
    }


    // 发送消息给服务器
    fun sendMsgToService() {
        object : HandlerThread("BatteryMsgService") {
            override fun onLooperPrepared() {
                super.onLooperPrepared()
                val msgClient = Message.obtain(null, 1, 1, 0)
                val looper = this.looper
                if (looper != null) {
                    msgClient.replyTo = Messenger(ClientHandler(looper))
                    msgClient.data = Bundle().apply {
                        this.putString(
                            "key",
                            gson.toJson(IPCLocationData(BatteryFinderDataCenter.locationRecordMap.toMap()))
                        )
                    }
                    Log.e(
                        Constant.TAG,
                        "isConn $isConn + ${gson.toJson(IPCLocationData(BatteryFinderDataCenter.locationRecordMap.toMap()))}"
                    )
                    if (isConn) {
                        kotlin.runCatching {
                            serviceMessenger?.send(msgClient)
                        }
                        Log.e(Constant.TAG, "serviceMessenger?.send(msgClient) ${msgClient.data} ")
                    }
                } else {
                    Log.e(Constant.TAG, "Looper is null,quit")
                    this.quitSafely()
                }
            }
        }.start()

    }

    // 绑定原本的消息
    fun bindServiceInvoked(context: Context) {
        if (MainProcessUtil.isMainProcess()) {
            return
        }
        val intent = Intent(context, LocationMessengerService::class.java)
        kotlin.runCatching {
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
            Log.e(Constant.TAG, "client message bindServiceInvoked ${Process.myPid()}")
        }
    }
}