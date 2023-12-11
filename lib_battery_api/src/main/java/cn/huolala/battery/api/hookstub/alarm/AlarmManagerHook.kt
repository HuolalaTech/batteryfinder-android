package cn.huolala.battery.api.hookstub.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.os.Build
import android.os.Handler
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.alarmRecordList
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc

@Keep
object AlarmManagerHook {
    @JvmStatic
    fun setAlarmClock(
        alarmManager: AlarmManager,
        info: AlarmManager.AlarmClockInfo,
        operation: PendingIntent,
        callCallName: String
    ) {
        BatteryCycleMonitor.funcWrapper("setAlarmClock") {
            alarmManager.setAlarmClock(info, operation)
        }
        recordList(callCallName, AlarmManager.RTC_WAKEUP, AlarmCallType.SETALARMCLOCK)
    }

    @JvmStatic
    fun setExact(
        alarmManager: AlarmManager,
        type: Int,
        triggerAtMillis: Long,
        operation: PendingIntent,
        callCallName: String
    ) {
        BatteryCycleMonitor.funcWrapper("setExact") {
            alarmManager.setExact(type, triggerAtMillis, operation)
        }
        recordList(callCallName, type, AlarmCallType.SETEXACT)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun setExact(
        alarmManager: AlarmManager,
        type: Int,
        triggerAtMillis: Long,
        tag: String,
        listener: AlarmManager.OnAlarmListener,
        targetHandler: Handler,
        callCallName: String
    ) {
        BatteryCycleMonitor.funcWrapper("setExact") {
            alarmManager.setExact(type, triggerAtMillis, tag, listener, targetHandler)
        }
        recordList(callCallName, type, AlarmCallType.SETEXACT)
    }

    @JvmStatic
    fun setExactAndAllowWhileIdle(
        alarmManager: AlarmManager,
        type: Int,
        triggerAtMillis: Long,
        operation: PendingIntent,
        callCallName: String
    ) {
        BatteryCycleMonitor.funcWrapper("setExactAndAllowWhileIdle") {
            alarmManager.setExactAndAllowWhileIdle(type, triggerAtMillis, operation)
        }
        recordList(callCallName, type, AlarmCallType.SETANDALLOWWHILEIDLE)
    }

    @JvmStatic
    private fun recordList(callCallName: String, type: Int, callType: AlarmCallType) {
        handleHookFunc {
            alarmRecordList.add(AlarmData(type, callType, callCallName).apply {
                this.createAtForeground = ApplicationLife.createAtForeground
            })
        }
    }


}