package cn.huolala.battery.api.hookstub.wakelock

import android.annotation.SuppressLint
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.WifiLock
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.wifiWakeLockRecordMap
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.NoRecordException
import cn.huolala.battery.api.hookstub.common.identity
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc
import com.battery.api.utils.handleStackTrace

@Keep
object WifiWakeLockHook {
    @SuppressLint("SoonBlockedPrivateApi")
    @JvmStatic
    fun acquire(wifiLock: WifiLock, acquireClass: String) {
        BatteryCycleMonitor.funcWrapper("acquire") {
            wifiLock.acquire()
        }
        handleHookFunc {
            if (wifiWakeLockRecordMap[wifiLock.identity()] == null) {
                val wakeLockData = WakeLockData()
                with(wakeLockData) {
                    kotlin.runCatching {
                        isRefCounted = isRefCounted(wifiLock)
                    }
                    holdClassName = acquireClass
                    createAtForeground = ApplicationLife.createAtForeground
                    startHoldTime = SystemClock.uptimeMillis()
                }
                wifiWakeLockRecordMap[wifiLock.identity()] = wakeLockData
            }

            // 这里需要单独记录startHoldTime，是因为当次的wakelock是可以被重复acquire的
            wifiWakeLockRecordMap[wifiLock.identity()]?.apply {
                acquireTime++
                // 如果是非引用计数，才需要更新时间
                if (!isRefCounted) {
                    startHoldTime = SystemClock.uptimeMillis()
                }
                lastAcquireTime = SystemClock.uptimeMillis()
                handleStackTrace {
                    registerClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recording
                BatteryFinderDataCenter.powerWakeLockRecordMap.updateRecord(
                    wifiLock.identity(),
                    this
                )
            }
        }

    }

    @JvmStatic
    fun release(wifiLock: WifiLock, releaseClass: String) {
        BatteryCycleMonitor.funcWrapper("release") {
            wifiLock.release()
        }
        handleHookFunc {
            if (wifiWakeLockRecordMap[wifiLock.identity()] == null && BatteryFinderDataCenter.dataConfig.isDebug) {
                throw NoRecordException()
            }
            wifiWakeLockRecordMap[wifiLock.identity()]?.apply {
                endHoldTime = SystemClock.uptimeMillis()
                useTime += endHoldTime - startHoldTime
                releaseTime++
                releaseClassName = releaseClass
                handleStackTrace {
                    releaseClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recorded
                BatteryFinderDataCenter.powerWakeLockRecordMap.deleteRecord(
                    wifiLock.identity(),
                    this
                )
            }
        }
    }

    // 部分oppo拿不到mRefCounted
    @SuppressLint("SoonBlockedPrivateApi")
    @JvmStatic
    private fun isRefCounted(wakeLock: WifiManager.WifiLock): Boolean {
        val wakeLockClass = WifiManager.WifiLock::class.java
        val mRefCounted = wakeLockClass.getDeclaredField("mRefCounted")
        mRefCounted.isAccessible = true
        return mRefCounted.get(wakeLock) as Boolean

    }
}