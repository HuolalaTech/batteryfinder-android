package cn.huolala.plugin.core;


import cn.huolala.plugin.core.utils.HookHelper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class AlarmHandler extends BaseHandler {

    static final String HOOKCLASS = "cn/huolala/battery/api/hookstub/alarm/AlarmManagerHook";
    static final List<String> setExactHookListDesc = new ArrayList<>();

    static {
        setExactHookListDesc.add("(IJLandroid/app/PendingIntent;)V");
        setExactHookListDesc.add("(IJLjava/lang/String;Landroid/app/AlarmManager$OnAlarmListener;Landroid/os/Handler;)V");
    }


    @Override
    void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method) {
        if (!node.owner.equals("android/app/AlarmManager")) {
            return;
        }
        if ((node.name.equals("setAlarmClock")) && node.desc.equals("(Landroid/app/AlarmManager$AlarmClockInfo;Landroid/app/PendingIntent;)V")) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if ((node.name.equals("setExact")) && setExactHookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if ((node.name.equals("setExactAndAllowWhileIdle")) && node.desc.equals("(IJLandroid/app/PendingIntent;)V")) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }
    }
}
