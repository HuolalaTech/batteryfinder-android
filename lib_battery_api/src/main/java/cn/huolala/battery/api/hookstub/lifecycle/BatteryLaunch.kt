package cn.huolala.battery.api.hookstub.lifecycle

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.os.SystemClock
import androidx.core.content.FileProvider
import androidx.lifecycle.ProcessLifecycleOwner
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.batterypower.DeviceBattery

class BatteryLaunch : FileProvider() {
    override fun onCreate(): Boolean {
        // app 前后台判断
        ProcessLifecycleOwner.get().lifecycle.addObserver(ApplicationLife())
        context?.runCatching {
            DeviceBattery.startRecord(this)
            BatteryFinderDataCenter.application = context
        }
        BatteryFinderDataCenter.launcherTime = SystemClock.uptimeMillis()
        // 一开始要记录一次当前的屏幕时间，不然就只能等下一次广播去更新
        BatteryFinderDataCenter.screenData.screenRecordTime = SystemClock.elapsedRealtime()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun getType(uri: Uri): String? {
        return ""
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }
}