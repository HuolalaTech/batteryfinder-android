package cn.huolala.battery.api.hookstub.cpu

import java.io.Serializable

/**
 * 当前cpu状态信息
 *
 * userTimeS：当前进程用户态消耗时间 systemTimeS当前进程内核状时间 childUserTimeS 等待子进程用户态  childSystemTimeS 等待子进程内核态
 */
data class CpuStatData(
    val recordPid: Int,
    val userTimeS: Double,
    val systemTimeS: Double,
    val childUserTimeS: Double,
    val childSystemTimeS: Double
) : Serializable
