[English Readme](README_English.md)

# BatteryFinder
如果你的App还在为高耗电而被用户投诉，却无法知道是哪里引发了高耗电，那么这款耗电检测工具BatteryFinder(电源探测者)应该可以帮到你。
它利用了编译时插桩的机制，将必要的功耗api进行hook，同时通过耗电基准文件进行加权得到耗电量，同时可通过堆栈打印，调用者跟踪等等分析手段
帮助开发者发现异常功耗问题。

目前支持：定位，蓝牙，Alarm，PowerWakeLock，WifiWakeLock，Sensor等多层次功耗检测

## 详细介绍
https://juejin.cn/post/7311343224546574346

## 使用介绍
### 编译引入插件
《声明》由于目前业务项目还在AGP版本还在7以下，所以当前仅支持7以下的插件，后续补充7以上的插件

todo 待上传地址
### gradle 配置
build.gradle里面添加插件依赖即可
```
apply plugin: 'com.battery.plugins'
```
同时选择开启插桩的类型，见app 中build.gradle
```
BatteryHookConfig{
// 因为以下涉及到hook操作，可根据配置进行是否开启，true则开启，默认都是false不开启
// Alarm闹钟 
  alarm = true   
//传感器
  sensor = true  
//蓝牙
  blueTooth = true 
//定位
  location = true 
//wakelock
  powerWakeLock = true 
  wifiWakeLock = true  、
// 设置不插桩的classname
  whiteList = ['whiteList1','whiteList2']
}
```

### 必要的框架初始化设置
#### step1

在Application 中进行必要的设置（几乎无损耗）
```
 BatteryFinderDataCenter.setConfig(
            // 是否是debug
            DataConfig.Builder()
                // 是否是debug模式，用于框架内部排查
                .setIsDebug(false)
                // 可选 是否记录多进程模式下的数据【仅支持定位】
                .setIsOpenIPC(true)
                // 是否运行打开耗电计算，可用于线上开关配置
                .setIsOpenBatteryFinder(true)
                // 是否打开堆栈跟踪，打开后会记录堆栈，如果用在频繁函数，存在一定的堆栈记录损耗
                .setOpenStackTrace(true)
                // 设置达到次数后才回调，0就是立即回调，配置设置于定位 【以下推荐默认值即可】
                .setLocationInvokeTime(0)
                // 等同于上，设置蓝牙达到次数
                .setBlueToothInvokeTime(0)
                // 等同于上，设置sensor达到次数
                .setSensorInvokeTime(0)
                // 等同于上，设置wakelock 达到次数
                .setPowerWakeLockInvokeTime(0)
                .setWifiWakeLockInvokeTime(0)
                // alarm 调度次数
                .setAlarmInvokeTime(0)
                .build()
        )
```

#### step2 

设置自己需要的基准电源文件（power_profile.xml）

在assets中放入power_profile.xml文件，如果不关心具体机型差异带来的功耗差异表现，可以直接用demo项目中的power_profile.xml文件，位于（app/src/main/assets）中

如果需要机型特定的power_profile.xml文件，可以通过反编译获取手机的
```
adb pull /system/framework/framework-res.apk
```
通过反编译framework-res.apk获取power_profile文件，放入assets即可

#### step3
定义一个实现DataChangeInvoke的类，里面可以选择复写方法，当有对应的数据来时，就会通过spi机制回调，参考【DemoInvoker】

比如调用requestLocationUpdates时 或者removeUpdates 时，都会回调一次onLocationInvoke，在里面可以获取功耗数据

```
override fun onLocationInvoke(
recordState: InvokeState,
locationReportData: LocationReportData,
batteryData: BatteryData,
cpuStatData: CpuStatData
) {
   super.onLocationInvoke(recordState, locationReportData, batteryData, cpuStatData)
   Log.e("demo_battery_finder","耗电量 已完成 + 记录中 ${locationReportData.getCurrentBatteryConsume()}")
   Log.e("demo_battery_finder","耗电量已完成 ${locationReportData.getRecordedBatteryConsume()}")
   Log.e("demo_battery_finder","耗电量记录中 ${locationReportData.getRecordingBatteryConsume()}")
   Log.e("demo_battery_finder","$recordState $locationReportData")
}
```

然后通过Android 标准SPI流程，在resources/META-INF/services/ 下建立一个com.battery.api.DataChangeInvoker文件，里面内容填写为自定义的实现DataChangeInvoke类名即可，可参考app目录

## 回调参数
| 数据类 | 含义 |
|-----|--|
| AlarmData | 记录闹钟的数据类，以下几类闹钟均被记录 setAlarmClock setExact setExactAndAllowWhileIdle |
| BatteryData | 通过广播记录的整机耗电，作为排查辅助 |
| ScreenData | 前台屏幕使用时间数据 |
| BlueToothData | 蓝牙使用时间与扫描模式ScanSettings 等|
| CpuStatData | 通过读取/proc/self/stat 获取当前的cpu数据，作为排查辅助 |
| LocationReportData | 持续定位耗电数据 包含多个子数据LocationBaseData |
| LocationBaseData  | 持续定位耗电子数据 以一个listener/pending intent为维度记录 |
| SensorData  | 传感器数据，使用时长/精度 |
| WakeLockData  | PowerWakeLock与WifiWakeLock等通用数据，记录使用时常，次数等 |


## 项目层级介绍
* **app下是使用例子**
* **lib_battery_api 是api层调用，也是hook的具体实现**
* **lib_battery_plugin 是asm 插桩的具体实现**

## 问题交流
如果你发现了bug或者有其他功能诉求，欢迎提issue。
如果想贡献代码，可以直接发起PR。

## 环境准备
建议直接用最新的稳定版本Android Studio打开工程.

## 许可证
采用Apache 2.0协议，详情参考[LICENSE](LICENSE)


