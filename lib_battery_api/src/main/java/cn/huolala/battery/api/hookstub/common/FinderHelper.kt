package com.battery.api.utils

import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter


fun handleHookFunc(hookFun: () -> Unit) {
    // 如果配置打开的情况下，才会运行hook函数
    if (BatteryFinderDataCenter.dataConfig.isOpenBatteryFinder) {
        hookFun.invoke()
    }
}

fun handleStackTrace(hookFun: () -> Unit) {
    if (BatteryFinderDataCenter.dataConfig.isOpenBatteryFinder) {
        hookFun.invoke()
    }
}