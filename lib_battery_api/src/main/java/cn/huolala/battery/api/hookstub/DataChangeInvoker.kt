package cn.huolala.battery.api.hookstub

import cn.huolala.battery.api.hookstub.alarm.AlarmData
import cn.huolala.battery.api.hookstub.batterypower.BatteryData
import cn.huolala.battery.api.hookstub.bluetooth.BlueToothData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.cpu.CpuStatData
import cn.huolala.battery.api.hookstub.location.LocationReportData
import cn.huolala.battery.api.hookstub.sensor.SensorData
import cn.huolala.battery.api.hookstub.wakelock.WakeLockData

interface DataChangeInvoker {
    fun onAlarmInvoke(
        alarmData: AlarmData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ){
        // empty
    }

    fun onBlueToothInvoke(
        recordState: InvokeState,
        blueToothData: BlueToothData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        // empty
    }

    fun onLocationInvoke(
        recordState: InvokeState,
        locationReportData: LocationReportData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        // empty
    }

    fun onSensorInvoke(
        recordState: InvokeState,
        sensorData: SensorData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        // empty
    }

    fun onPowerWakeLockInvoke(
        recordState: InvokeState,
        powerWakeLock: WakeLockData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        // empty
    }

    fun onWifiWakeLockInvoke(
        recordState: InvokeState,
        wifiWakeLock: WakeLockData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        // empty
    }
}