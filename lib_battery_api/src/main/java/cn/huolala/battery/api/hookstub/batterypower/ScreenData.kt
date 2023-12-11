package cn.huolala.battery.api.hookstub.batterypower

import cn.huolala.battery.api.hookstub.common.BatteryConsumer
import java.io.Serializable

class ScreenData : BatteryConsumer, Serializable {
    // 记录屏幕的时间
    var screenOnTotalTime = 0L

    // 需要记录一下，开始时间
    var screenRecordTime = 0L
    
}