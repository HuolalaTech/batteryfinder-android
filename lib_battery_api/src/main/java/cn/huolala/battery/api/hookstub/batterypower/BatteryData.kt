package cn.huolala.battery.api.hookstub.batterypower

import java.io.Serializable

class BatteryData : Serializable {
    var startRecordBatteryLevel = 0f
    var endRecordBatteryLevel = 0f
    var batteryChargingTime = 0L
    var batteryRealTime = 0L

    // 消耗的电量百分比：包括了充电时记录
    fun getUseBatteryPercentage(): Float {
        return startRecordBatteryLevel - endRecordBatteryLevel
    }

    override fun toString(): String {
        return "BatteryData(startRecordBatteryLevel=$startRecordBatteryLevel, endRecordBatteryLevel=$endRecordBatteryLevel, batteryChargingTime=$batteryChargingTime, batteryRealTime=$batteryRealTime)"
    }


}