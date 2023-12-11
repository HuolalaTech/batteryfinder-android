package cn.huolala.plugin.core.utils;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

/**
 * 替换调用指令
 */
public class HookHelper {
    static public void replaceNode(MethodInsnNode node, ClassNode klass, MethodNode method, String owner) {
        LdcInsnNode ldc = new LdcInsnNode(klass.name);
        method.instructions.insertBefore(node, ldc);
        node.setOpcode(Opcodes.INVOKESTATIC);
        int anchorIndex = node.desc.indexOf(")");
        String subDesc = node.desc.substring(anchorIndex);
        String origin = node.desc.substring(1, anchorIndex);
        node.desc = "(L" + node.owner + ";" + origin + "Ljava/lang/String;" + subDesc;
        node.owner = owner;
        System.out.println("replaceNode result is " + node.desc);
    }
}
