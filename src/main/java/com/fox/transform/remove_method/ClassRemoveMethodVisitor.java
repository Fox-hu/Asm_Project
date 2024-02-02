package com.fox.transform.remove_method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @Author fox
 * @Date 2024/2/3 0:11
 */
class ClassRemoveMethodVisitor extends ClassVisitor {
    private final String methodName;
    private final String methodDesc;

    public ClassRemoveMethodVisitor(int api, ClassVisitor cv, String methodName, String methodDesc) {
        super(api, cv);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    //是目标方法 直接返回null 则不生成方法体 从而删除方法
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        if (name.equals(methodName) && descriptor.equals(methodDesc)) {
            return null;
        } else {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
