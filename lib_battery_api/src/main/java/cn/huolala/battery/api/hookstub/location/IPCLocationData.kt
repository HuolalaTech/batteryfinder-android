package cn.huolala.battery.api.hookstub.location

import java.io.Serializable

data class IPCLocationData(val map: Map<String, LocationReportData>) : Serializable