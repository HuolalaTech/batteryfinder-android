package cn.huolala.battery.api.hookstub.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.location.Criteria
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.locationRecordMap
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.MainProcessUtil
import cn.huolala.battery.api.hookstub.common.NoRecordException
import cn.huolala.battery.api.hookstub.common.identity
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc
import com.battery.api.utils.handleStackTrace

@Keep
object LocationManagerHook {
    const val TAG = "LocationManagerHook"

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun requestLocationUpdates(
        manager: LocationManager,
        provider: String,
        minTime: Long,
        minDistance: Float,
        listener: LocationListener,
        className: String
    ) {
        BatteryCycleMonitor.funcWrapper("requestLocationUpdates") {
            manager.requestLocationUpdates(provider, minTime, minDistance, listener)
        }
        recordParams(
            listener.identity(),
            className,
            minTime,
            minDistance,
            provider,
        )
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun requestLocationUpdates(
        manager: LocationManager,
        provider: String,
        minTimeMs: Long,
        minDistanceM: Float,
        listener: LocationListener,
        looper: Looper,
        className: String
    ) {
        BatteryCycleMonitor.funcWrapper("requestLocationUpdates") {
            manager.requestLocationUpdates(provider, minTimeMs, minDistanceM, listener, looper)
        }
        recordParams(
            listener.identity(),
            className,
            minTimeMs,
            minDistanceM,
            provider = provider,
        )
    }


    @SuppressLint("MissingPermission")
    @JvmStatic
    fun requestLocationUpdates(
        manager: LocationManager,
        minTimeMs: Long,
        minDistanceM: Float,
        criteria: Criteria,
        listener: LocationListener,
        looper: Looper,
        className: String
    ) {
        BatteryCycleMonitor.funcWrapper("requestLocationUpdates") {
            manager.requestLocationUpdates(minTimeMs, minDistanceM, criteria, listener, looper)
        }
        recordParams(
            listener.identity(),
            className,
            minTimeMs,
            minDistanceM,
        )
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun requestLocationUpdates(
        manager: LocationManager,
        provider: String,
        minTimeMs: Long,
        minDistanceM: Float,
        pendingIntent: PendingIntent,
        className: String
    ) {
        BatteryCycleMonitor.funcWrapper("requestLocationUpdates") {
            manager.requestLocationUpdates(provider, minTimeMs, minDistanceM, pendingIntent)
        }
        recordParams(
            pendingIntent.identity(),
            className,
            minTimeMs,
            minDistanceM,
            provider
        )
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun requestLocationUpdates(
        manager: LocationManager,
        minTimeMs: Long,
        minDistanceM: Float,
        criteria: Criteria,
        pendingIntent: PendingIntent,
        className: String
    ) {
        BatteryCycleMonitor.funcWrapper("requestLocationUpdates") {
            manager.requestLocationUpdates(minTimeMs, minDistanceM, criteria, pendingIntent)
        }
        recordParams(
            pendingIntent.identity(),
            className,
            minTimeMs,
            minDistanceM
        )
    }


    @JvmStatic
    fun removeUpdates(
        manager: LocationManager, listener: LocationListener, releaseClassName: String
    ) {
        windParams(listener.identity(), releaseClassName)
        // 真正停止listener
        BatteryCycleMonitor.funcWrapper("removeUpdates") {
            manager.removeUpdates(listener)
        }
    }


    @JvmStatic
    fun removeUpdates(
        manager: LocationManager, pendingIntent: PendingIntent, releaseClassName: String
    ) {
        windParams(pendingIntent.identity(), releaseClassName)
        // 真正停止listener
        BatteryCycleMonitor.funcWrapper("removeUpdates") {
            manager.removeUpdates(pendingIntent)
        }
    }

    private fun recordParams(
        hashCode: String,
        className: String,
        minTimeMs: Long,
        minDistanceM: Float,
        provider: String = LocationManager.GPS_PROVIDER,
    ) {
        handleHookFunc {
            if (locationRecordMap[hashCode] == null) {
                locationRecordMap[hashCode] = LocationReportData().apply {
                    createAtForeground = ApplicationLife.createAtForeground
                }
            }
            locationRecordMap[hashCode]?.let {
                it.locationList.add(
                    LocationBaseData(
                        minTimeMs,
                        minDistanceM,
                        provider,
                        startTime = SystemClock.uptimeMillis(),
                        registerClassName = className
                    ).apply {
                        handleStackTrace {
                            this.registerStackTrace = Log.getStackTraceString(Throwable())
                        }
                    }
                )
                it.state = InvokeState.Recording
                locationRecordMap.updateRecord(hashCode, it)
            }


            // 非主线程才需要发送
            if (!MainProcessUtil.isMainProcess() && BatteryFinderDataCenter.dataConfig.isOpenIPC) {
                ClientMessenger.sendMsgToService()
            }
        }
    }

    private fun windParams(hashCode: String, releaseClassName: String) {
        handleHookFunc {
            if (locationRecordMap[hashCode] == null) {
                takeIf { BatteryFinderDataCenter.dataConfig.isDebug }.let {
                    throw NoRecordException()
                }
            }
            locationRecordMap[hashCode]?.apply {
                // 当进行一次释放锁操作的时候，应该直接就把当前的已开始定位的子项进行一次结算
                locationList.forEach { base ->
                    base.useTime = SystemClock.uptimeMillis() - base.startTime
                    base.releaseClassName = releaseClassName
                    base.apply {
                        handleStackTrace {
                            this.releaseStackTrace = Log.getStackTraceString(Throwable())
                        }
                    }
                }
                this.state = InvokeState.Recorded
                locationRecordMap.deleteRecord(hashCode, this)
            }

            // 非主线程才需要发送
            if (!MainProcessUtil.isMainProcess() && BatteryFinderDataCenter.dataConfig.isOpenIPC) {
                ClientMessenger.sendMsgToService()
            }
        }
    }


}