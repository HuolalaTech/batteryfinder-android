package cn.huolala.battery.api.hookstub

import android.os.Process
import android.util.Log
import cn.huolala.battery.api.hookstub.common.Constant
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.random.Random

object BatteryCycleMonitor {

    private val funSet = CopyOnWriteArraySet<String>()

    private fun onFuncEntry(name: String) {
        if (funSet.contains(name)) {
            Log.e(Constant.TAG, "found cycle dependents ,kill process by 5")
            Process.sendSignal(Process.myPid(), 5)
        } else {
            funSet.add(name)
        }
    }

    private fun onFuncExit(name: String) {
        funSet.remove(name)
    }

    fun funcWrapper(name: String, func: () -> Unit) {
        if (!BatteryFinderDataCenter.dataConfig.isDebug) {
            func.invoke()
            return
        }
        val seed = Random.nextDouble(0.0, 100000.0)
        onFuncEntry(Thread.currentThread().id.toString() + name + seed)
        func.invoke()
        onFuncExit(Thread.currentThread().id.toString() + name + seed)
    }
}