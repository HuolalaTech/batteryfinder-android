package cn.huolala.battery.api.hookstub.wakelock

import android.os.SystemClock
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.common.BatteryConsumer
import cn.huolala.battery.api.hookstub.common.InvokeData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.timeFormat
import java.io.Serializable

class WakeLockData : InvokeData(), BatteryConsumer, Serializable {
    // acquire 方法调用次数
    var acquireTime: Int = 0

    // 释放次数
    var releaseTime: Int = 0

    // 最终持有唤醒的时间
    var useTime: Long = 0L

    // 这里是每一个请求的时间
    var startHoldTime: Long = 0L

    // 最后一次请求时间
    var lastAcquireTime: Long = 0L

    // 这里是每一次释放的时间
    var endHoldTime: Long = 0L

    // 是否采用了引用计数
    var isRefCounted = true

    // 针对调用acquire(long timeout)却不调用release 的场景
    var autoReleaseByTimeOver: Long = 0L

    // 自动release 次数
    var autoReleaseTime: Int = 0

    var holdClassName: String = ""

    var releaseClassName: String = ""


    // WakeLock 是否已经被释放
    private fun isRelease(): Boolean {
        if (!isRefCounted) {
            // 非引用计数，一次删除即可，同时要确保最后记录时间>开始记录时间
            if (releaseTime > 0 && endHoldTime > lastAcquireTime) {
                return true
            }
        } else {
            if (acquireTime == releaseTime) {
                return true
            }
            // 如果acquire的次数 == releaseTime && 超时删除acquire已超时
            if ((acquireTime - autoReleaseTime) == releaseTime && SystemClock.uptimeMillis() - autoReleaseByTimeOver > 0) {
                return true
            }
        }
        return false
    }


    // 如果被释放了，那么实际使用时间就是useTime
    // 如果没有被释放，那么实际使用时间就是useTime + SystemClock.uptimeMillis() - startHoldTime
    override fun getRecordingBatteryConsume(): Double {
        if (state == InvokeState.Recorded) {
            return 0.0
        }
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        if (!isRelease()) {
            val realUseTime = useTime + SystemClock.uptimeMillis() - startHoldTime
            return (realUseTime * profile.wakeLockPower).timeFormat()
        }
        return 0.0
    }

    override fun getRecordedBatteryConsume(): Double {
        val profile = BatteryFinderDataCenter.powerProfile ?: return 0.0
        if (state == InvokeState.Recording) {
            return 0.0
        }
        if (isRelease()) {
            return (useTime * profile.wakeLockPower).timeFormat()
        }
        return 0.0
    }


    override fun toString(): String {
        return "WakeLockData(acquireTime=$acquireTime, releaseTime=$releaseTime, useTime=$useTime, isRefCounted=$isRefCounted, autoReleaseByTimeOver=$autoReleaseByTimeOver, autoReleaseTime=$autoReleaseTime, holdClassName='$holdClassName', releaseClassName='$releaseClassName')"
    }

}