package cn.huolala.battery.api.hookstub.parser

class BatteryPowerProfile {
    var gps: Double = 0.0
    var wakeLockPower: Double = 0.0
    var capacity: Double = 0.0
    var blueTooth: Double = 0.0
    var wifiOn: Double = 0.0
    var screenOnPower: Double = 0.0

    override fun toString(): String {
        return "BatteryPowerProfile(gps=$gps, wakeLockPower=$wakeLockPower, capacity=$capacity, blueTooth=$blueTooth, wifiOn=$wifiOn, screenOnPower=$screenOnPower)"
    }


}