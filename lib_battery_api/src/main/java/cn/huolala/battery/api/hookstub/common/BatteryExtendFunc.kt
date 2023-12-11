package cn.huolala.battery.api.hookstub.common

fun Double.timeFormat(): Double {
    return this / (1000 * 60 * 60)
}


fun Any.identity():String{
    return this.javaClass.name + "@" + Integer.toHexString(System.identityHashCode(this))
}