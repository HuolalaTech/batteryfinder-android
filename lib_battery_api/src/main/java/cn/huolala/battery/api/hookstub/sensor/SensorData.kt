package cn.huolala.battery.api.hookstub.sensor

import android.os.SystemClock
import cn.huolala.battery.api.hookstub.common.BatteryConsumer
import cn.huolala.battery.api.hookstub.common.InvokeData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.timeFormat
import java.io.Serializable

class SensorData : InvokeData(), BatteryConsumer, Serializable {
    var samplingPeriodUs = 0

    var maxReportLatencyUs = 0

    var registerClassName = ""

    var unregisterClassName = ""

    // 时间
    var startTime: Long = 0L

    var endTime: Long = 0L

    var isWakeLock = false

    var sensorType: Int = 0

    var sensorPower: Double = 0.0


    override fun getRecordingBatteryConsume(): Double {
        if (state == InvokeState.Recorded) {
            return 0.0
        }
        val useTime = SystemClock.uptimeMillis() - startTime
        return (useTime * sensorPower).timeFormat()
    }

    override fun getRecordedBatteryConsume(): Double {
        if (state == InvokeState.Recording) {
            return 0.0
        }
        return (endTime * sensorPower).timeFormat()
    }


    override fun toString(): String {
        return "SensorData(samplingPeriodUs=$samplingPeriodUs, maxReportLatencyUs=$maxReportLatencyUs, isWakeLock = $isWakeLock  type = $sensorType registerClassName='$registerClassName', unregisterClassName='$unregisterClassName', startTime=$startTime, totalTime=$endTime)"
    }

}