package com.example.batteryfinder

import android.util.Log
import cn.huolala.battery.api.hookstub.DataChangeInvoker
import cn.huolala.battery.api.hookstub.alarm.AlarmData
import cn.huolala.battery.api.hookstub.batterypower.BatteryData
import cn.huolala.battery.api.hookstub.bluetooth.BlueToothData
import cn.huolala.battery.api.hookstub.common.InvokeState
import cn.huolala.battery.api.hookstub.cpu.CpuStatData
import cn.huolala.battery.api.hookstub.location.LocationReportData
import cn.huolala.battery.api.hookstub.sensor.SensorData
import cn.huolala.battery.api.hookstub.wakelock.WakeLockData

class DemoInvoker : DataChangeInvoker {
    override fun onLocationInvoke(
        recordState: InvokeState,
        locationReportData: LocationReportData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onLocationInvoke(recordState, locationReportData, batteryData, cpuStatData)
        Log.e(
            "demo_battery_finder",
            "耗电量 已完成 + 记录中 ${locationReportData.getCurrentBatteryConsume()}"
        )
        Log.e(
            "demo_battery_finder",
            "耗电量已完成 ${locationReportData.getRecordedBatteryConsume()}"
        )
        Log.e(
            "demo_battery_finder",
            "耗电量记录中 ${locationReportData.getRecordingBatteryConsume()}"
        )
        Log.e("demo_battery_finder", "$recordState $locationReportData")
    }

    override fun onPowerWakeLockInvoke(
        recordState: InvokeState,
        powerWakeLock: WakeLockData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onPowerWakeLockInvoke(recordState, powerWakeLock, batteryData, cpuStatData)
        Log.e(
            "demo_battery_finder",
            "耗电量 已完成 + 记录中 ${powerWakeLock.getCurrentBatteryConsume()}"
        )
        Log.e("demo_battery_finder", "耗电量已完成 ${powerWakeLock.getRecordedBatteryConsume()}")
        Log.e("demo_battery_finder", "耗电量记录中 ${powerWakeLock.getRecordingBatteryConsume()}")
        Log.e("demo_battery_finder", "$recordState $powerWakeLock")
    }

    override fun onWifiWakeLockInvoke(
        recordState: InvokeState,
        wifiWakeLock: WakeLockData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onWifiWakeLockInvoke(recordState, wifiWakeLock, batteryData, cpuStatData)
        Log.e(
            "demo_battery_finder",
            "耗电量 已完成 + 记录中 ${wifiWakeLock.getCurrentBatteryConsume()}"
        )
        Log.e("demo_battery_finder", "耗电量已完成 ${wifiWakeLock.getRecordedBatteryConsume()}")
        Log.e("demo_battery_finder", "耗电量记录中 ${wifiWakeLock.getRecordingBatteryConsume()}")
        Log.e("demo_battery_finder", "$recordState $wifiWakeLock")
    }

    override fun onSensorInvoke(
        recordState: InvokeState,
        sensorData: SensorData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onSensorInvoke(recordState, sensorData, batteryData, cpuStatData)
        Log.e(
            "demo_battery_finder",
            "耗电量 已完成 + 记录中 ${sensorData.getCurrentBatteryConsume()}"
        )
        Log.e("demo_battery_finder", "耗电量已完成 ${sensorData.getRecordedBatteryConsume()}")
        Log.e("demo_battery_finder", "耗电量记录中 ${sensorData.getRecordingBatteryConsume()}")
        Log.e("demo_battery_finder", "$recordState $sensorData")
    }

    override fun onBlueToothInvoke(
        recordState: InvokeState,
        blueToothData: BlueToothData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onBlueToothInvoke(recordState, blueToothData, batteryData, cpuStatData)
        Log.e(
            "demo_battery_finder",
            "耗电量 已完成 + 记录中 ${blueToothData.getCurrentBatteryConsume()}"
        )
        Log.e("demo_battery_finder", "耗电量已完成 ${blueToothData.getRecordedBatteryConsume()}")
        Log.e("demo_battery_finder", "耗电量记录中 ${blueToothData.getRecordingBatteryConsume()}")
        Log.e("demo_battery_finder", "$recordState $blueToothData")
    }

    override fun onAlarmInvoke(
        alarmData: AlarmData,
        batteryData: BatteryData,
        cpuStatData: CpuStatData
    ) {
        super.onAlarmInvoke(alarmData, batteryData, cpuStatData)
        Log.e("demo_battery_finder", "$alarmData")
        Log.e("demo_battery_finder", "BatteryData $batteryData")
        Log.e("demo_battery_finder", "CpuStatData $cpuStatData")

    }
}