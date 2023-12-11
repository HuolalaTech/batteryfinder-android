package cn.huolala.battery.api.hookstub.common

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter

object MainProcessUtil {

    fun isMainProcess(): Boolean {
        val context = BatteryFinderDataCenter.application ?: return false
        if (context.packageName == getProcessName(context)) {
            return true
        }
        return false
    }

    private fun getProcessName(context: Context): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            return Application.getProcessName()
        }
        val pid = Process.myPid()
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfo = am.runningAppProcesses ?: return ""

        runningAppProcessInfo.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return ""

    }


}