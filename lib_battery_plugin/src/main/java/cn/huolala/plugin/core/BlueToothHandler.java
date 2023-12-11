package cn.huolala.plugin.core;


import cn.huolala.plugin.core.utils.HookHelper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class BlueToothHandler extends BaseHandler {

    static final String HOOKCLASS = "cn/huolala/battery/api/hookstub/bluetooth/BlueToothHook";
    static final List<String> startScanHookListDesc = new ArrayList<>();
    static final List<String> stopScanHookListDesc = new ArrayList<>();

    static {
        // 添加需要hook的所有定位方法签名
        startScanHookListDesc.add("(Ljava/util/List;Landroid/bluetooth/le/ScanSettings;Landroid/bluetooth/le/ScanCallback;)V");
        startScanHookListDesc.add("(Landroid/bluetooth/le/ScanCallback;)V");
        startScanHookListDesc.add("(Ljava/util/List;Landroid/bluetooth/le/ScanSettings;Landroid/app/PendingIntent;)I");
        stopScanHookListDesc.add("(Landroid/bluetooth/le/ScanCallback;)V");
        stopScanHookListDesc.add("(Landroid/app/PendingIntent;)V");
    }


    @Override
    void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method) {
        // 处理 acquire 跟 release
        if (!node.owner.equals("android/bluetooth/le/BluetoothLeScanner")) {
            return;
        }
        if ((node.name.equals("startScan")) && startScanHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if ((node.name.equals("stopScan")) && stopScanHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

    }
}
