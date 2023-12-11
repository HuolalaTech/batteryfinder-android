package cn.huolala.battery.api.hookstub.cpu

import android.system.Os
import android.system.OsConstants
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter
import cn.huolala.battery.api.hookstub.BatteryFinderDataCenter.cpuStatData
import com.battery.api.utils.handleHookFunc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.CoroutineContext

object CpuManager {
    private const val statPath = "/proc/self/stat"

    //http://man7.org/linux/man-pages/man5/proc.5.html
    // 第14个开始是有效信息
    private const val userTimeIndex = 13

    private val file = File(statPath)

    fun recordCpuStat() {
        handleHookFunc {
            recordCpuStatInternal(Dispatchers.Main)
        }
    }

    private fun recordCpuStatInternal(context: CoroutineContext) {
        CoroutineScope(Dispatchers.IO).launch {
            val cpuStat = file.readText()
            withContext(context) {
                val list = cpuStat.split(" ").toList()
                val click = Os.sysconf(OsConstants._SC_CLK_TCK)
                if (cpuStatData.recordPid != 0 && list[0].trim()
                        .toInt() != cpuStatData.recordPid && BatteryFinderDataCenter.dataConfig.isDebug
                ) {
                    // 上次记录的pid跟此时记录的pid不一致
                    throw NoSamePidException()
                }
                runCatching {
                    cpuStatData = CpuStatData(
                        list[0].trim().toInt(),
                        list[userTimeIndex].trim().toDouble() / click,
                        list[userTimeIndex + 1].trim().toDouble() / click,
                        list[userTimeIndex + 2].trim()
                            .toDouble() / click,
                        list[userTimeIndex + 3].trim()
                            .toDouble() / click
                    )
                }
            }

        }
    }
}