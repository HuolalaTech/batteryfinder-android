package cn.huolala.battery.api.hookstub.bluetooth

import android.bluetooth.le.ScanSettings
import android.os.SystemClock
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.common.BatteryConsumer
import cn.huolala.battery.api.hookstub.common.InvokeData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.timeFormat
import java.io.Serializable

class BlueToothData : InvokeData(), BatteryConsumer, Serializable {

    var startTime: Long = 0L
    var endTime: Long = 0L
    var startScanClass: String = ""
    var stopScanClass: String = ""
    var hasStop = true

    // 默认就是没有setting就是 0
    var scanMode: Int = ScanSettings.SCAN_MODE_LOW_POWER

    override fun getRecordingBatteryConsume(): Double {
        if (state == InvokeState.Recorded) {
            return 0.0
        }
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        val endTime = SystemClock.uptimeMillis() - startTime
        return (endTime * profile.blueTooth).timeFormat()

    }

    override fun getRecordedBatteryConsume(): Double {
        if (state == InvokeState.Recording) {
            return 0.0
        }
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        return (endTime * profile.blueTooth).timeFormat()
    }

    override fun toString(): String {
        return "BlueToothData(startTime=$startTime, endTime=$endTime, startScanClass='$startScanClass', stopScanClass='$stopScanClass', hasStop=$hasStop, scanMode=$scanMode)"
    }


}