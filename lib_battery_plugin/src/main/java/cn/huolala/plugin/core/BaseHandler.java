package cn.huolala.plugin.core;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public abstract class BaseHandler {

    abstract void transformInvokeVirtual(MethodInsnNode node, ClassNode klass, MethodNode method);
}
