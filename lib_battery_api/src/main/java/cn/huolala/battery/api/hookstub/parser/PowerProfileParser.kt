package cn.huolala.battery.api.hookstub.parser

import android.util.Log
import android.util.Xml
import cn.huolala.battery.api.hookstub.common.Constant
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream

object PowerProfileParser {

    fun parseXml(inputStream: InputStream?): BatteryPowerProfile? {
        if (inputStream == null) {
            Log.e(
                Constant.TAG,
                "inputStream is null,make sure power_profile.xml is in the asset!"
            )
            return null
        }
        val powerProfile: BatteryPowerProfile = BatteryPowerProfile()
        val parser = Xml.newPullParser()
        parser.setInput(inputStream, "utf-8")
        var type = parser.eventType
        while (type != XmlPullParser.END_DOCUMENT) {
            when (type) {
                XmlPullParser.START_TAG -> {
                    if ("item" == parser.name) {
                        // 先拿属性后next
                        when (parser.getAttributeValue(null, "name")) {
                            "bluetooth.on" -> powerProfile.blueTooth = parser.nextText().toDouble()
                            "gps.on" -> powerProfile.gps = parser.nextText().toDouble()
                            "cpu.idle" -> powerProfile.wakeLockPower = parser.nextText().toDouble()
                            "battery.capacity" -> powerProfile.capacity =
                                parser.nextText().toDouble()

                            "wifi.on" -> powerProfile.wifiOn = parser.nextText().toDouble()
                            "screen.on" -> powerProfile.screenOnPower = parser.nextText().toDouble()

                        }
                    }
                }
            }
            //继续往下读取标签类型
            type = parser.next()
        }
        return powerProfile
    }
}