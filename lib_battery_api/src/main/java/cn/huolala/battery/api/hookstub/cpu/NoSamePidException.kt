package cn.huolala.battery.api.hookstub.cpu

class NoSamePidException : Exception("last pid != record pid") {
}