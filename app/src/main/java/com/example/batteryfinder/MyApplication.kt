package com.example.batteryfinder

import android.app.Application
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.DataConfig

/**
- @copyright：深圳依时货拉拉科技有限公司
- @fileName: MyApplication
- @author: chenhailiang
- @date: 2023/2/16
- @description:
- @history:
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        BatteryFinderDataCenter.setConfig(
            // 是否是debug
            DataConfig.Builder()
                .setIsDebug(true)
                // 可选 是否记录多进程模式下的数据
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

    }
}