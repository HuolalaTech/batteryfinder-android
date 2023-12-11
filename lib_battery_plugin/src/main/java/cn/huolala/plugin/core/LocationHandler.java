package cn.huolala.plugin.core;


import cn.huolala.plugin.core.utils.HookHelper;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.ArrayList;
import java.util.List;

public class LocationHandler extends BaseHandler {

    static final String LocationManager = "android/location/LocationManager";
    static final String REQUESTLOCATIONUPDATE = "requestLocationUpdates";
    static final String REMOVEUPDATES = "removeUpdates";
    static final String HOOKCLASS = "cn/huolala/battery/api/hookstub/location/LocationManagerHook";
    static final List<String> hookListDesc = new ArrayList<>();

    static {
        // 添加需要hook的所有定位方法签名
        hookListDesc.add("(Ljava/lang/String;JFLandroid/location/LocationListener;)V");
        hookListDesc.add("(Ljava/lang/String;JFLandroid/location/LocationListener;Landroid/os/Looper;)V");
        hookListDesc.add("(Ljava/lang/String;JFLjava/util/concurrent/Executor;Landroid/location/LocationListener;)V");
        hookListDesc.add("(JFLandroid/location/Criteria;Landroid/location/LocationListener;Landroid/os/Looper;)V");
        hookListDesc.add("(JFLandroid/location/Criteria;Ljava/util/concurrent/Executor;Landroid/location/LocationListener;)V");
        hookListDesc.add("(Ljava/lang/String;JFLandroid/app/PendingIntent;)V");
        hookListDesc.add("(JFLandroid/location/Criteria;Landroid/app/PendingIntent;)V");
        hookListDesc.add("(Ljava/lang/String;Landroid/location/LocationRequest;Ljava/util/concurrent/Executor;Landroid/location/LocationListener;)V");
        hookListDesc.add("(Ljava/lang/String;Landroid/location/LocationRequest;Landroid/app/PendingIntent;)V");


    }


    @Override
    void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method) {
        if (!node.owner.equals(LocationManager)) {
            return;
        }
        if (node.name.equals(REQUESTLOCATIONUPDATE) && hookListDesc.contains(node.desc)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        // removeupdate hook
        if (node.desc.equals("(Landroid/location/LocationListener;)V") && node.name.equals(REMOVEUPDATES)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }

        if (node.desc.equals("(Landroid/app/PendingIntent;)V") && node.name.equals(REMOVEUPDATES)) {
            HookHelper.replaceNode(node, klass, method, HOOKCLASS);
        }
    }
}
