package cn.huolala.battery.api.hookstub

import android.content.Context
import cn.huolala.battery.api.hookstub.alarm.AlarmData
import cn.huolala.battery.api.hookstub.batterypower.BatteryData
import cn.huolala.battery.api.hookstub.batterypower.DeviceBattery
import cn.huolala.battery.api.hookstub.batterypower.ScreenData
import cn.huolala.battery.api.hookstub.bluetooth.BlueToothData
import cn.huolala.battery.api.hookstub.common.InvokeArrayList
import cn.huolala.battery.api.hookstub.common.InvokeHashMap
import cn.huolala.battery.api.hookstub.cpu.CpuManager
import cn.huolala.battery.api.hookstub.cpu.CpuStatData
import cn.huolala.battery.api.hookstub.location.LocationReportData
import cn.huolala.battery.api.hookstub.parser.BatteryPowerProfile
import cn.huolala.battery.api.hookstub.parser.PowerProfileParser
import cn.huolala.battery.api.hookstub.sensor.SensorData
import cn.huolala.battery.api.hookstub.wakelock.WakeLockData
import java.util.ServiceLoader

object BatteryFinderDataCenter {

    var dataConfig: DataConfig = DataConfig()
        private set

    var application: Context? = null
    var launcherTime: Long = 0L

    fun setConfig(dataConfig: DataConfig) {
        BatteryFinderDataCenter.dataConfig = dataConfig
        if (dataConfig.isOpenIPC) {
            application?.let { BatteryIPCOption.openLocationIPC(it) }
        }
    }

    // 功耗文件数据
    val powerProfile: BatteryPowerProfile? by lazy {
        PowerProfileParser.parseXml(application?.resources?.assets?.open("power_profile.xml"))
    }


    // 电量数据
    val batteryData = BatteryData()


    // 屏幕display电量
    val screenData = ScreenData()

    // 当前记录运行cpu时长
    var cpuStatData = CpuStatData(0, 0.0, 0.0, 0.0, 0.0)


    //alarm数据
    val alarmRecordList = InvokeArrayList<AlarmData>(block = {
        this.count() > (dataConfig.alarmInvokeTime)
    })
    { state, value ->
        handlerInvoke {
            it.onAlarmInvoke(value, batteryData, cpuStatData)
        }

    }

    // 蓝牙 scan数据
    val scanCallbackMap = InvokeHashMap<String, BlueToothData>({
        this.count() > (dataConfig.blueToothInvokeTime)
    }) { recordState, value ->
        handlerInvoke {
            it.onBlueToothInvoke(
                recordState, value, batteryData, cpuStatData
            )
        }
    }

    //location 定位数据
    val locationRecordMap = InvokeHashMap<String, LocationReportData>({
        this.count() > (dataConfig.locationInvokeTime)
    }) { recordState, value ->
        handlerInvoke {
            // 处于可归档状态的数据进行归档
            it.onLocationInvoke(
                recordState, value, batteryData, cpuStatData
            )
        }
    }


    // 传感器数据
    val sensorRecordMap =
        InvokeHashMap<String, SensorData>({
            this.count() > (dataConfig.sensorInvokeTime)
        }) { recordState, value ->
            handlerInvoke {
                it.onSensorInvoke(recordState, value, batteryData, cpuStatData)
            }

        }

    // powerWakeLock
    val powerWakeLockRecordMap = InvokeHashMap<String, WakeLockData>({
        this.count() > (dataConfig.powerWakeLockInvokeTime)
    }) { recordState, value ->
        handlerInvoke {
            it.onPowerWakeLockInvoke(recordState, value, batteryData, cpuStatData)
        }
    }

    // wifiWakeLock
    val wifiWakeLockRecordMap = InvokeHashMap<String, WakeLockData>({
        this.count() > (dataConfig.wifiWakeLockInvokeTime)
    }) { recordState, value ->
        handlerInvoke {
            it.onWifiWakeLockInvoke(recordState, value, batteryData, cpuStatData)
        }
    }


    private fun handlerInvoke(invoker: (dataChangeInvoker: DataChangeInvoker) -> Unit) {
        // invoke 之前dump出电量数据与cpu数据
        application?.let { DeviceBattery.endRecord(it) }
        CpuManager.recordCpuStat()

        ServiceLoader.load(DataChangeInvoker::class.java).forEach {
            if (it is DataChangeInvoker) {
                invoker.invoke(it)
            }
        }
    }


}