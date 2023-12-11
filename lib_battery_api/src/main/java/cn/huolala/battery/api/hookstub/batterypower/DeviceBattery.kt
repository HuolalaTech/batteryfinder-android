package cn.huolala.battery.api.hookstub.batterypower

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.batteryData
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.screenData
import com.battery.api.utils.handleHookFunc

object DeviceBattery {

    var lastUpdateTime = 0L

    private val intentFilter = IntentFilter().also {
        it.addAction(Intent.ACTION_POWER_CONNECTED)
        it.addAction(Intent.ACTION_POWER_DISCONNECTED)
        it.addAction(Intent.ACTION_SCREEN_ON)
        it.addAction(Intent.ACTION_SCREEN_OFF)
    }

    private val batteryReceiver = BatteryBroadCastReceiver() { time, action ->
        when (action) {
            Intent.ACTION_POWER_CONNECTED -> {
                batteryData.batteryChargingTime =
                    batteryData.batteryChargingTime + time - lastUpdateTime
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                batteryData.batteryRealTime =
                    batteryData.batteryChargingTime + time - lastUpdateTime
            }
            // 监听屏幕
            Intent.ACTION_SCREEN_ON -> {
                screenData.screenRecordTime = time
            }
            // 每次熄屏的时候，会更新一次数据
            Intent.ACTION_SCREEN_OFF -> {
                screenData.screenOnTotalTime += time - screenData.screenRecordTime
            }
        }

        lastUpdateTime = time

    }

    fun startRecord(context: Context) {
        handleHookFunc {
            context.runCatching {
                registerReceiver(batteryReceiver, intentFilter)
                // ACTION_BATTERY_CHANGED  是一个粘性广播，可以直接拿到数据
                val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (level < 0 || scale < 0) {
                    batteryData.startRecordBatteryLevel = 0F
                } else {
                    batteryData.startRecordBatteryLevel = level.toFloat() / scale * 100
                }
            }
        }

    }


    fun endRecord(context: Context) {
        handleHookFunc {
            context.runCatching {
                val intent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
                val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
                val scale = intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
                if (level < 0 || scale < 0) {
                    batteryData.endRecordBatteryLevel = 0F
                } else {
                    batteryData.endRecordBatteryLevel = level.toFloat() / scale * 100
                }
            }
        }

    }
}