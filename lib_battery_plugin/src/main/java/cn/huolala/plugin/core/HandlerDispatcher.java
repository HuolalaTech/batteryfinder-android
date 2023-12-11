package cn.huolala.plugin.core;


import cn.huolala.plugin.core.config.WhileList;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.ArrayList;
import java.util.List;


/**
 * - @copyright：深圳依时货拉拉科技有限公司
 * - @fileName: HandlerDispacther
 * - @author: chenhailiang
 * - @date: 2023/7/3
 * - @description:
 * - @history:
 */
public class HandlerDispatcher {
    private final List<BaseHandler> handlers = new ArrayList<>();

    public HandlerDispatcher(boolean openLocationHook, boolean openBlueToothHook, boolean openSensorHook, boolean openPowerWakelockHook, boolean openWifiWakeLockHook, boolean openAlarmHook) {
        if (openLocationHook) {
            handlers.add(new LocationHandler());
        }

        if (openBlueToothHook) {
            handlers.add(new BlueToothHandler());
        }

        if (openSensorHook) {
            handlers.add(new SensorHandler());
        }

        if (openPowerWakelockHook) {
            handlers.add(new PowerWakeLockHandler());
        }

        if (openWifiWakeLockHook) {
            handlers.add(new WifiWakeLockHandler());
        }
        if (openAlarmHook) {
            handlers.add(new AlarmHandler());
        }
        System.out.println("battery config is " +
                "openLocationHook:" + openLocationHook +
                " openBlueToothHook:" + openBlueToothHook +
                " openSensorHook:" + openSensorHook +
                " openPowerWakelockHook:" + openPowerWakelockHook +
                " openWifiWakeLockHook:" + openWifiWakeLockHook +
                " openAlarmHook:" + openAlarmHook);
    }

    public void handlerMethodNode(ClassNode klass) {
        boolean match = WhileList.getInstance().getWhiteList().stream().anyMatch(s -> klass.name.contains(s));
        if (match) {
            return;
        }
        klass.methods.forEach(methodNode -> methodNode.instructions.forEach(abstractInsnNode -> {
            // 如果是INVOKEVIRTUAL才继续进行
            if (abstractInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                handlers.forEach(baseHandler -> baseHandler.transformInvokeVirtual((MethodInsnNode) abstractInsnNode, klass, methodNode));
            }
        }));
    }

}
