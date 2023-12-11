package cn.huolala.plugin.core;


import cn.huolala.plugin.core.utils.HookHelper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class PowerWakeLockHandler extends BaseHandler {
    static final String HOOKCLASS = "cn/huolala/battery/api/hookstub/wakelock/PowerWakeLockHook";
    static final List<String> acquireHookListDesc = new ArrayList<>();
    static final List<String> releaseHookListDesc = new ArrayList<>();

    static {
        // 添加需要hook的所有定位方法签名
        acquireHookListDesc.add("()V");
        acquireHookListDesc.add("(J)V");
        releaseHookListDesc.add("()V");
        releaseHookListDesc.add("(I)V");
    }


    @Override
    void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method) {
        if (!node.owner.equals("android/os/PowerManager$WakeLock")) {
            return;
        }
        // 处理 acquire 跟 release
        if ((node.name.equals("acquire")) && acquireHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if ((node.name.equals("release")) && releaseHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }
    }
}
