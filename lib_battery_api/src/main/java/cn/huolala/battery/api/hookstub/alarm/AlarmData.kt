package cn.huolala.battery.api.hookstub.alarm

import cn.huolala.battery.api.hookstub.common.InvokeData
import cn.huolala.battery.api.hookstub.common.InvokeState
import java.io.Serializable


data class AlarmData(val alarmType: Int, val alarmCallType: AlarmCallType, val callClass: String) :
    InvokeData(), Serializable {

    init {
        state = InvokeState.Recorded
    }


    override fun getRecordingBatteryConsume(): Double {
        return 0.0
    }

    override fun getRecordedBatteryConsume(): Double {
        return 0.0
    }
}


enum class AlarmCallType {
    SETALARMCLOCK, SETANDALLOWWHILEIDLE, SETEXACT
}