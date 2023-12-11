package cn.huolala.battery.api.hookstub.bluetooth

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.Build
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import androidx.annotation.RequiresApi
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.scanCallbackMap
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.NoRecordException
import cn.huolala.battery.api.hookstub.common.identity
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc
import com.battery.api.utils.handleStackTrace

@Keep
object BlueToothHook {
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun startScan(scanner: BluetoothLeScanner, scanCallback: ScanCallback, callClass: String) {
        recordParams(scanCallback.identity(), callClass)
        BatteryCycleMonitor.funcWrapper("startScan") {
            scanner.startScan(scanCallback)
        }
    }

    @SuppressLint("MissingPermission")
    @JvmStatic
    fun startScan(
        scanner: BluetoothLeScanner,
        filters: List<ScanFilter>?,
        settings: ScanSettings,
        scanCallback: ScanCallback,
        callClass: String
    ) {
        recordParams(scanCallback.identity(), callClass, settings.scanMode)
        BatteryCycleMonitor.funcWrapper("startScan") {
            scanner.startScan(filters, settings, scanCallback)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun startScan(
        scanner: BluetoothLeScanner,
        filters: List<ScanFilter>?,
        settings: ScanSettings,
        callbackPendingIntent: PendingIntent,
        callClass: String
    ): Int {
        recordParams(callbackPendingIntent.identity(), callClass, settings.scanMode)
        var result = 0
        BatteryCycleMonitor.funcWrapper("startScan") {
            result = scanner.startScan(filters, settings, callbackPendingIntent)
        }
        return result
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingPermission")
    @JvmStatic
    fun stopScan(
        scanner: BluetoothLeScanner, callbackPendingIntent: PendingIntent,
        stopClass: String
    ) {
        BatteryCycleMonitor.funcWrapper("stopScan") {
            scanner.stopScan(callbackPendingIntent)
        }
        windParams(callbackPendingIntent.identity(), stopClass)
    }


    @SuppressLint("MissingPermission")
    @JvmStatic
    fun stopScan(
        scanner: BluetoothLeScanner, callback: ScanCallback,
        stopClass: String
    ) {
        BatteryCycleMonitor.funcWrapper("stopScan") {
            scanner.stopScan(callback)
        }
        windParams(callback.identity(), stopClass)
    }

    private fun recordParams(
        hashCode: String,
        callClass: String,
        scanMode: Int = ScanSettings.SCAN_MODE_LOW_POWER
    ) {
        handleHookFunc {
            if (scanCallbackMap[hashCode] == null) {
                scanCallbackMap[hashCode] = BlueToothData().also {
                    it.startScanClass = callClass
                    it.scanMode = scanMode
                    it.createAtForeground = ApplicationLife.createAtForeground
                }
            }
            scanCallbackMap[hashCode]?.apply {
                startTime = SystemClock.uptimeMillis()
                state = InvokeState.Recording
                handleStackTrace {
                    this.registerClassStack = Log.getStackTraceString(Throwable())
                }
                scanCallbackMap.updateRecord(hashCode, this)
            }
        }
    }

    private fun windParams(hashCode: String, stopClass: String) {
        handleHookFunc {
            if (scanCallbackMap[hashCode] == null) {
                takeIf { BatteryFinderDataCenter.dataConfig.isDebug }.let {
                    throw NoRecordException()
                }
            }
            scanCallbackMap[hashCode]?.apply {
                stopScanClass = stopClass
                endTime = SystemClock.uptimeMillis() - startTime
                state = InvokeState.Recorded
                handleStackTrace {
                    this.releaseClassStack = Log.getStackTraceString(Throwable())
                }
                scanCallbackMap.deleteRecord(hashCode, this)
            }
        }
    }

}