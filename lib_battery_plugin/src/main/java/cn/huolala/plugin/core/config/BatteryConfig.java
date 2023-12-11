package cn.huolala.plugin.core.config;

import java.util.List;

public class BatteryConfig {
    public boolean alarm = false;
    public boolean blueTooth = false;
    public boolean location = false;
    public boolean powerWakeLock = false;
    public boolean wifiWakeLock = false;
    public boolean sensor = false;

    public List<String> whiteList;

    @Override
    public String toString() {
        return "BatteryConfig{" +
                "alarm=" + alarm +
                ", blueTooth=" + blueTooth +
                ", location=" + location +
                ", powerWakeLock=" + powerWakeLock +
                ", wifiWakeLock=" + wifiWakeLock +
                ", sensor=" + sensor +
                ", whiteList=" + whiteList +
                '}';
    }
}
