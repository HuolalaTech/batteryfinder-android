package cn.huolala.battery.api.hookstub.location

import android.location.LocationManager
import android.os.Process
import android.os.SystemClock
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.common.BatteryConsumer
import cn.huolala.battery.api.hookstub.common.InvokeData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.timeFormat
import java.io.Serializable

class LocationReportData : InvokeData(), BatteryConsumer, Serializable {
    val locationList = mutableListOf<LocationBaseData>()
    val pid: Int = Process.myPid()


    override fun getRecordingBatteryConsume(): Double {
        // gps 定位，网络定位，混合定位被标记成了废弃，其他就是被动监听，消耗为0
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        if (state == InvokeState.Recorded) {
            return 0.0
        }

        var totalPower = 0.0
        locationList.forEach {
            val power = when (it.provider) {
                LocationManager.NETWORK_PROVIDER -> profile.wifiOn
                LocationManager.GPS_PROVIDER -> profile.gps
                LocationManager.PASSIVE_PROVIDER -> 0.0
                else -> (profile.wifiOn + profile.gps) / 2
            }

            //如果当前使用时间没有确定，那么使用时间就等于当前计数时间 - 开始时间，否则就是当前子项的使用时间
            val useTime = SystemClock.uptimeMillis() - it.startTime
            // 这里为什么不计算minTime这些参数，是因为request之后，会持有定位wakelock，实际上会一直保持，这里直接讲请求时长作为计算
            totalPower += (useTime * power).timeFormat()
        }

        return totalPower
    }

    override fun getRecordedBatteryConsume(): Double {
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        if (state == InvokeState.Recording) {
            return 0.0
        }
        var totalPower = 0.0
        locationList.forEach {
            val power = when (it.provider) {
                LocationManager.NETWORK_PROVIDER -> profile.wifiOn
                LocationManager.GPS_PROVIDER -> profile.gps
                LocationManager.PASSIVE_PROVIDER -> 0.0
                else -> (profile.wifiOn + profile.gps) / 2
            }

            //如果当前使用时间没有确定，那么使用时间就等于当前计数时间 - 开始时间，否则就是当前子项的使用时间
            val useTime = it.useTime
            // 这里为什么不计算minTime这些参数，是因为request之后，会持有定位wakelock，实际上会一直保持，这里直接讲请求时长作为计算
            totalPower += (useTime * power).timeFormat()
        }
        return totalPower
    }


    override fun toString(): String {
        return "LocationReportData(pid = '$pid',locationList={${locationList},pid = ${pid}})"
    }


}
