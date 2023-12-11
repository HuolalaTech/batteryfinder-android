package cn.huolala.plugin.core;


import cn.huolala.plugin.core.utils.HookHelper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class SensorHandler extends BaseHandler {
    static final String HOOKCLASS = "cn/huolala/battery/api/hookstub/sensor/SensorHook";
    static final List<String> registerListenerHookListDesc = new ArrayList<>();
    static final List<String> unregisterListenerHookListDesc = new ArrayList<>();

    static {
        // 添加需要hook的所有定位方法签名
        registerListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;I)Z");
        registerListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;II)Z");
        registerListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;ILandroid/os/Handler;)Z");
        registerListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;IILandroid/os/Handler;)Z");
        unregisterListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;)V");
        unregisterListenerHookListDesc.add("(Landroid/hardware/SensorEventListener;Landroid/hardware/Sensor;)V");
    }

    @Override
    void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method) {
        if (!node.owner.equals("android/hardware/SensorManager")) {
            return;
        }
        // 处理 acquire 跟 release
        if ((node.name.equals("registerListener")) && registerListenerHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if ((node.name.equals("unregisterListener")) && unregisterListenerHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }
    }
}
