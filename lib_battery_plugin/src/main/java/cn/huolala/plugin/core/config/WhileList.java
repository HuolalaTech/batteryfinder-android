package cn.huolala.plugin.core.config;

import java.util.ArrayList;
import java.util.List;

public class WhileList {
    private static final List<String> whiteList = new ArrayList<>();

    private static final WhileList instance = new WhileList();

    private WhileList() {
        whiteList.add("cn/huolala/battery/api/hookstub/location/LocationManagerHook");
        whiteList.add("cn/huolala/battery/api/hookstub/cpu/CpuManager");
        whiteList.add("cn/huolala/battery/api/hookstub/wakelock/PowerWakeLockHook");
        whiteList.add("cn/huolala/battery/api/hookstub/bluetooth/BlueToothHook");
        whiteList.add("cn/huolala/battery/api/hookstub/sensor/SensorHook");
        whiteList.add("cn/huolala/battery/api/hookstub/wakelock/WifiWakeLockHook");
        whiteList.add("cn/huolala/battery/api/hookstub/alarm/AlarmManagerHook");
    }

    public static WhileList getInstance() {
        return instance;
    }

    public static void addWhiteList(List<String> list) {
        whiteList.addAll(list);
        System.out.println(whiteList);
    }

    public List<String> getWhiteList() {
        return whiteList;
    }

}
