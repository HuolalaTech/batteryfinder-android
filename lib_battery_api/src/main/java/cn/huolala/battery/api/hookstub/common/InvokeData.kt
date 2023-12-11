package cn.huolala.battery.api.hookstub.common

abstract class InvokeData {
    var createAtForeground = false
    var registerClassStack = ""
    var releaseClassStack = ""
    var state = InvokeState.Recording


    fun getCurrentBatteryConsume(): Double {
        return getRecordedBatteryConsume() + getRecordingBatteryConsume()
    }

    abstract fun getRecordingBatteryConsume(): Double

    abstract fun getRecordedBatteryConsume(): Double

}