
# BatteryFinder
If your App is still getting complaints from users about high power consumption, but you can't figure out what's causing it, this BatteryFinder tool should be able to help. It uses the mechanism of piling at compile time, hooks the necessary power api, and obtains power consumption by weighting the power consumption benchmark file. At the same time, it can help developers find abnormal power consumption problems by stack printing, caller tracking and other analytical means.

Current support: location, Bluetooth, Alarm, PowerWakeLock, WifiWakeLock, Sensor and other multi-level power detection

## Detailed introduction
待补充

## Getting started
### Compile and import plugin
todo 待上传地址
### gradle configuration
add this code in build.gradle
```
apply plugin: 'com.battery.plugins'
```
you can choose the type of plugin‘s configuration.You can see the example in app build.gradle

```
BatteryHookConfig{
// Because the hook operation is involved below, it can be enabled or not according to the configuration, and true is enabled.By the way,default value is false.
// Alarm 
  alarm = true   
// Sensor
  sensor = true  
// BlueTooth
  blueTooth = true 
// Location
  location = true 
//wakelock
  powerWakeLock = true 
  wifiWakeLock = true  、
// whitelist:The class name with this prefix will be kept
  whiteList = ['whiteList1','whiteList2']
}
```

### Initialize
#### step1

You can init the library in Application 
```
 BatteryFinderDataCenter.setConfig(
            DataConfig.Builder()
                // Whether to open debug mode
                .setIsDebug(false)
                // Optional Whether to record data in multi-process mode [Location only]
                .setIsOpenIPC(true)
                // Whether to run on power consumption calculation
                .setIsOpenBatteryFinder(true)
                // Whether to turn on stack trace. After turning on stack trace, the stack will be recorded
                .setOpenStackTrace(true)
                // Set the number of times before callback, 0 is immediately callback, the configuration is set to positioning [the following recommended default value can be]
                .setLocationInvokeTime(0)
                // Set the number of times in bluetooth running
                .setBlueToothInvokeTime(0)
                // Set the number of times in sensor running
                .setSensorInvokeTime(0)
                //  Set the number of times in wakelock running
                .setPowerWakeLockInvokeTime(0)
                .setWifiWakeLockInvokeTime(0)
                // Set the number of times in alarm running
                .setAlarmInvokeTime(0)
                .build()
        )
```

#### step2 

Set the base power file you need（power_profile.xml）

put the power_profile.xml in your assets.If you are not concerned about power consumption differences caused by model differences, you can directly use the power_profile.xml file in the demo project, which is located in app/src/main/assets

If you need the model-specific power_profile.xml file, you can decompile it to get in this path
```
adb pull /system/framework/framework-res.apk
```
Decompile framework-res.apk to obtain the power_profile file and put it in assets 【if you need】

#### step3
Define a class that implements DataChangeInvoke, which can select the carbon method, and when the corresponding data comes, it will be called back through the spi mechanism, refer to [DemoInvoker]


example：When requestLocationUpdates or removeUpdates are called, an onLocationInvoke is called to retrieve the power consumption data

```
override fun onLocationInvoke(
recordState: InvokeState,
locationReportData: LocationReportData,
batteryData: BatteryData,
cpuStatData: CpuStatData
) {
   super.onLocationInvoke(recordState, locationReportData, batteryData, cpuStatData)
   Log.e("demo_battery_finder","recorded + recording ${locationReportData.getCurrentBatteryConsume()}")
   Log.e("demo_battery_finder","recorded ${locationReportData.getRecordedBatteryConsume()}")
   Log.e("demo_battery_finder","recording ${locationReportData.getRecordingBatteryConsume()}")
   Log.e("demo_battery_finder","$recordState $locationReportData")
}
```

And then through the Android standard SPI process, under the resources/meta-inf/services/build a com. '. API. DataChangeInvoker file, Fill in the content as a custom implementation DataChangeInvoke class name, you can refer to the app directory

## Callback parameter
| Data class  | meaning |
|-----|--|
| AlarmData | Data classes that record alarms. The following types of alarms are recorded setAlarmClock setExact setExactAndAllowWhileIdle |
| BatteryData | The power consumption of the whole machine recorded by the broadcast is used as a check aid |
| ScreenData | Foreground screen usage time data |
| BlueToothData | Bluetooth usage time and scanning mode ScanSettings|
| CpuStatData | Get the current cpu data by reading /proc/self.stat as a troubleshooting aid |
| LocationReportData | The persistent location power consumption data contains multiple sub-data LocationBaseData |
| LocationBaseData  | Persistent location consuming electronic data is recorded in a listener/pending intent dimension |
| SensorData  | Sensor data, duration/accuracy |
| WakeLockData  | PowerWakeLock and WifiWakeLock and other general data, record the frequency of use, the number of times |


## Project level introduction
* **app is an example to use BatteryFinder**
* **lib_battery_api is an api layer call and a specific implementation of hook**
* **lib_battery_core is a concrete implementation of asm hook**

## Communication
- If you find a bug, open an issue.
- If you have a feature request, open an issue.
- If you want to contribute, submit a pull request.

## Environmental preparation
It is recommended to open the project directly with the latest stable version of Android Studio.

## License
BatteryFinder is released under the Apache 2.0 license. See [LICENSE](LICENSE) for details.


