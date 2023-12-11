package cn.huolala.battery.api.hookstub.location

import java.io.Serializable


data class LocationBaseData(
    var minTimeMs: Long = 0L,
    var minDistanceM: Float = 0F,
    val provider: String = "unknown",
    val startTime: Long = 0L,
    var useTime: Long = 0L,
    var registerClassName: String = "",
    var releaseClassName: String = "",
    var registerStackTrace: String = "",
    var releaseStackTrace: String = ""
) : Serializable {
}
