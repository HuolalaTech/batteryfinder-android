package cn.huolala.battery.api.hookstub.wakelock

import android.annotation.SuppressLint
import android.os.PowerManager.WakeLock
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.powerWakeLockRecordMap
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.NoRecordException
import cn.huolala.battery.api.hookstub.common.identity
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc
import com.battery.api.utils.handleStackTrace

@Keep
object PowerWakeLockHook {
    /**
    只有满足以下条件，才会真正请求,因此，如果是非引用计数，那么都更新请求时间，如果是第一次，也更新
    if (!mRefCounted || mInternalCount == 1) {
    mHandler.removeCallbacks(mReleaser);
    Trace.asyncTraceBegin(Trace.TRACE_TAG_POWER, mTraceName, 0);
    try {
    mService.acquireWakeLock(mToken, mFlags, mTag, mPackageName, mWorkSource,
    mHistoryTag, mDisplayId);
    } catch (RemoteException e) {
    throw e.rethrowFromSystemServer();
    }
    mHeld = true;
    }
     */

    @JvmStatic
    fun acquire(wakeLock: WakeLock, className: String) {
        BatteryCycleMonitor.funcWrapper("acquire") {
            wakeLock.acquire()
        }
        handleHookFunc {
            if (powerWakeLockRecordMap[wakeLock.identity()] == null) {
                val wakeLockData = WakeLockData()
                with(wakeLockData) {
                    holdClassName = className
                    runCatching {
                        isRefCounted = isRefCounted(wakeLock)
                    }
                    createAtForeground = ApplicationLife.createAtForeground
                    // startHoldTime 只能被记录一次
                    startHoldTime = SystemClock.uptimeMillis()
                }

                powerWakeLockRecordMap[wakeLock.identity()] = wakeLockData
            }

            powerWakeLockRecordMap[wakeLock.identity()]?.apply {
                acquireTime++
                if (!isRefCounted) {
                    startHoldTime = SystemClock.uptimeMillis()
                }
                lastAcquireTime = SystemClock.uptimeMillis()
                handleStackTrace {
                    registerClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recording
                powerWakeLockRecordMap.updateRecord(wakeLock.identity(), this)
            }
        }


    }

    @JvmStatic
    fun acquire(wakeLock: WakeLock, time: Long, className: String) {
        BatteryCycleMonitor.funcWrapper("acquire") {
            wakeLock.acquire(time)
        }
        handleHookFunc {
            if (powerWakeLockRecordMap[wakeLock.identity()] == null) {
                val wakeLockData = WakeLockData()
                with(wakeLockData) {
                    holdClassName = className
                    runCatching {
                        isRefCounted = isRefCounted(wakeLock)
                    }
                    createAtForeground = ApplicationLife.createAtForeground
                }
                powerWakeLockRecordMap[wakeLock.identity()] = wakeLockData
            }
            powerWakeLockRecordMap[wakeLock.identity()]?.apply {
                acquireTime++
                autoReleaseTime++
                autoReleaseByTimeOver = SystemClock.uptimeMillis() + time

                // 如果是非引用计数，才需要更新时间
                if (!isRefCounted) {
                    startHoldTime = SystemClock.uptimeMillis()
                }
                lastAcquireTime = SystemClock.uptimeMillis()
                handleStackTrace {
                    registerClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recording
                powerWakeLockRecordMap.updateRecord(wakeLock.identity(), this)
            }
        }


    }

    @JvmStatic
    fun release(wakeLock: WakeLock, className: String) {
        BatteryCycleMonitor.funcWrapper("release") {
            wakeLock.release()
        }
        handleHookFunc {
            if (powerWakeLockRecordMap[wakeLock.identity()] == null && BatteryFinderDataCenter.dataConfig.isDebug) {
                throw NoRecordException()
            }
            powerWakeLockRecordMap[wakeLock.identity()]?.apply {
                endHoldTime = SystemClock.uptimeMillis()
                useTime += endHoldTime - startHoldTime
                releaseTime++
                releaseClassName = className
                handleStackTrace {
                    this.releaseClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recorded
                powerWakeLockRecordMap.deleteRecord(wakeLock.identity(), this)
            }
        }

    }

    @JvmStatic
    fun release(wakeLock: WakeLock, flags: Int, className: String) {
        BatteryCycleMonitor.funcWrapper("release") {
            wakeLock.release(flags)
        }
        handleHookFunc {
            if (powerWakeLockRecordMap[wakeLock.identity()] == null && BatteryFinderDataCenter.dataConfig.isDebug) {
                throw NoRecordException()
            }
            powerWakeLockRecordMap[wakeLock.identity()]?.apply {
                endHoldTime = SystemClock.uptimeMillis()
                useTime += endHoldTime - startHoldTime
                releaseTime++
                releaseClassName = className
                handleStackTrace {
                    this.releaseClassStack = Log.getStackTraceString(Throwable())
                }
                this.state = InvokeState.Recorded
                powerWakeLockRecordMap.deleteRecord(wakeLock.identity(), this)
            }

        }

    }

    @SuppressLint("SoonBlockedPrivateApi")
    @JvmStatic
    private fun isRefCounted(wakeLock: WakeLock): Boolean {
        val wakeLockClass = WakeLock::class.java
        val mRefCounted = wakeLockClass.getDeclaredField("mRefCounted")
        mRefCounted.isAccessible = true
        return mRefCounted.get(wakeLock) as Boolean

    }


}