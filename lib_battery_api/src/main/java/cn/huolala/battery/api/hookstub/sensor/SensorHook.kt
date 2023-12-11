package cn.huolala.battery.api.hookstub.sensor

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.annotation.Keep
import cn.huolala.battery.api.hookstub.BatteryCycleMonitor
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.sensorRecordMap
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.common.identity
import cn.huolala.battery.api.hookstub.lifecycle.ApplicationLife
import com.battery.api.utils.handleHookFunc
import com.battery.api.utils.handleStackTrace

@Keep
object SensorHook {

    @JvmStatic
    fun registerListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        sensor: Sensor?,
        samplingPeriodUs: Int,
        registerClassName: String
    ): Boolean {
        var result = false
        BatteryCycleMonitor.funcWrapper("registerListener") {
            result = sensorManager.registerListener(listener, sensor, samplingPeriodUs)
        }
        if (result && listener != null && sensor != null) {
            recordParams(
                listener.identity(),
                sensor.isWakeUpSensor,
                samplingPeriodUs,
                registerClassName,
                type = sensor.type,
                sensorPower = sensor.power.toDouble(),
            )
        }
        return result
    }

    @JvmStatic
    fun registerListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        sensor: Sensor?,
        samplingPeriodUs: Int,
        maxReportLatencyUs: Int,
        registerClassName: String
    ): Boolean {
        var result = false
        BatteryCycleMonitor.funcWrapper("registerListener") {
            result = sensorManager.registerListener(
                listener,
                sensor,
                samplingPeriodUs,
                maxReportLatencyUs
            )
        }
        if (result && listener != null && sensor != null) {
            recordParams(
                listener.identity(),
                sensor.isWakeUpSensor,
                samplingPeriodUs,
                registerClassName,
                maxReportLatencyUs,
                type = sensor.type,
                sensorPower = sensor.power.toDouble(),
            )


        }
        return result
    }

    @JvmStatic
    fun registerListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        sensor: Sensor?,
        samplingPeriodUs: Int,
        handler: Handler,
        registerClassName: String
    ): Boolean {

        var result = false
        BatteryCycleMonitor.funcWrapper("registerListener") {
            result = sensorManager.registerListener(listener, sensor, samplingPeriodUs, handler)
        }
        if (result && listener != null && sensor != null) {
            recordParams(
                listener.identity(),
                sensor.isWakeUpSensor,
                samplingPeriodUs,
                registerClassName,
                type = sensor.type,
                sensorPower = sensor.power.toDouble(),
            )
        }
        return result
    }

    @JvmStatic
    fun registerListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        sensor: Sensor?,
        samplingPeriodUs: Int,
        maxReportLatencyUs: Int,
        handler: Handler,
        registerClassName: String
    ): Boolean {
        var result = false
        BatteryCycleMonitor.funcWrapper("registerListener") {
            result = sensorManager.registerListener(
                listener,
                sensor,
                samplingPeriodUs,
                maxReportLatencyUs,
                handler
            )
        }
        if (result && listener != null && sensor != null) {
            recordParams(
                listener.identity(),
                sensor.isWakeUpSensor,
                samplingPeriodUs,
                registerClassName,
                maxReportLatencyUs,
                type = sensor.type,
                sensorPower = sensor.power.toDouble(),
            )
        }
        return result
    }

    @JvmStatic
    fun unregisterListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        unregisterClassName: String
    ) {
        BatteryCycleMonitor.funcWrapper("unregisterListener") {
            sensorManager.unregisterListener(listener)
        }

        // 避免非isWakeUpSensor的 sensor干扰
        if (listener != null) {
            windParams(listener.identity(), unregisterClassName)
        }

    }

    @JvmStatic
    fun unregisterListener(
        sensorManager: SensorManager,
        listener: SensorEventListener?,
        sensor: Sensor?,
        unregisterClassName: String
    ) {
        BatteryCycleMonitor.funcWrapper("unregisterListener") {
            sensorManager.unregisterListener(listener, sensor)
        }
        if (listener != null && sensor != null) {
            windParams(listener.identity(), unregisterClassName)
        }
    }

    private fun recordParams(
        hashCode: String,
        isWakeLock: Boolean,
        samplingPeriodUs: Int,
        registerClassName: String,
        maxReportLatencyUs: Int = 0,
        type: Int,
        sensorPower: Double,
    ) {
        handleHookFunc {
            if (sensorRecordMap[hashCode] == null) {
                sensorRecordMap[hashCode] = SensorData().apply {
                    this.isWakeLock = isWakeLock
                    startTime = SystemClock.uptimeMillis()
                    this.samplingPeriodUs = samplingPeriodUs
                    this.maxReportLatencyUs = maxReportLatencyUs
                    this.registerClassName = registerClassName
                    createAtForeground = ApplicationLife.createAtForeground
                    sensorType = type
                    this.sensorPower = sensorPower
                    handleStackTrace {
                        this.registerClassStack = Log.getStackTraceString(Throwable())
                    }
                    this.state = InvokeState.Recording
                    sensorRecordMap.updateRecord(hashCode, this)
                }
            }
        }

    }

    private fun windParams(hashCode: String, unregisterClassName: String) {
        handleHookFunc {
            if (sensorRecordMap[hashCode] != null) {
                sensorRecordMap[hashCode]?.apply {
                    endTime = SystemClock.uptimeMillis() - startTime
                    this.unregisterClassName = unregisterClassName
                    handleStackTrace {
                        this.releaseClassStack = Log.getStackTraceString(Throwable())
                    }
                    this.state = InvokeState.Recorded
                    sensorRecordMap.deleteRecord(hashCode, this)
                }
            }
        }
    }


}