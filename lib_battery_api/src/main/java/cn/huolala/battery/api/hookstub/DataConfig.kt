package cn.huolala.battery.api.hookstub

class DataConfig(
    var isDebug: Boolean = false,
    var isOpenBatteryFinder: Boolean = false,
    var openStackTrace: Boolean = false,
    var isOpenIPC: Boolean = false,
    val alarmInvokeTime: Int = 0,
    val blueToothInvokeTime: Int = 0,
    val locationInvokeTime: Int = 0,
    val sensorInvokeTime: Int = 0,
    val powerWakeLockInvokeTime: Int = 0,
    val wifiWakeLockInvokeTime: Int = 0,
) {
    class Builder {
        private var isDebug: Boolean = false
        private var isOpenBatteryFinder: Boolean = false
        private var openStackTrace: Boolean = true
        private var isOpenIPC: Boolean = false
        private var alarmInvokeTime: Int = 0
        private var blueToothInvokeTime: Int = 0
        private var locationInvokeTime: Int = 0
        private var sensorInvokeTime: Int = 0
        private var powerWakeLockInvokeTime: Int = 0
        private var wifiWakeLockInvokeTime: Int = 0


        fun setIsDebug(isDebug: Boolean): Builder {
            this.isDebug = isDebug
            return this
        }

        fun setIsOpenBatteryFinder(isOpenBatteryFinder: Boolean): Builder {
            this.isOpenBatteryFinder = isOpenBatteryFinder
            return this
        }

        fun setOpenStackTrace(openStackTrace: Boolean): Builder {
            this.openStackTrace = openStackTrace
            return this
        }

        fun setIsOpenIPC(isOpenIPC: Boolean): Builder {
            this.isOpenIPC = isOpenIPC
            return this
        }

        fun setAlarmInvokeTime(alarmInvokeTime: Int): Builder {
            this.alarmInvokeTime = alarmInvokeTime
            return this
        }

        fun setBlueToothInvokeTime(blueToothInvokeTime: Int): Builder {
            this.blueToothInvokeTime = blueToothInvokeTime
            return this
        }

        fun setLocationInvokeTime(locationInvoke: Int): Builder {
            this.locationInvokeTime = locationInvoke
            return this
        }

        fun setSensorInvokeTime(sensorInvokeTime: Int): Builder {
            this.sensorInvokeTime = sensorInvokeTime
            return this
        }

        fun setPowerWakeLockInvokeTime(powerWakeLockInvokeTime: Int): Builder {
            this.powerWakeLockInvokeTime = powerWakeLockInvokeTime
            return this
        }

        fun setWifiWakeLockInvokeTime(wifiWakeLockInvokeTime: Int): Builder {
            this.wifiWakeLockInvokeTime = wifiWakeLockInvokeTime
            return this
        }


        fun build(): DataConfig {
            return DataConfig(
                isDebug = isDebug,
                isOpenBatteryFinder = isOpenBatteryFinder,
                openStackTrace = openStackTrace,
                isOpenIPC = isOpenIPC,
                alarmInvokeTime = alarmInvokeTime,
                blueToothInvokeTime = blueToothInvokeTime,
                locationInvokeTime = locationInvokeTime,
                sensorInvokeTime = sensorInvokeTime,
                powerWakeLockInvokeTime = powerWakeLockInvokeTime,
                wifiWakeLockInvokeTime = wifiWakeLockInvokeTime,
            )
        }
    }

}
