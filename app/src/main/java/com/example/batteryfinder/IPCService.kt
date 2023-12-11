package com.example.batteryfinder

import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Process
import android.util.Log

/**
- @copyright：深圳依时货拉拉科技有限公司
- @fileName: IPCService
- @author: chenhailiang
- @date: 2023/2/16
- @description: 验证多进程
- @history:
 */
class IPCService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val manager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener3 = LocationListener {
            Log.e("hello", "get location ${it.provider}")
        }
        Log.e("hello", "service ${Process.myPid()}")
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            manager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10L,
                10f,
                locationListener3
            )
            Log.e("hello", "requestLocationUpdates")
        }, 3000)

    }
}