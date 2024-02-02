package com.fox.transform.add_method;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

/**
 * @Author fox
 * @Date 2024/2/2 23:40
 */
abstract class ClassAddMethodVisitor extends ClassVisitor {
    private final int methodAccess;
    private final String methodName;
    private final String methodDesc;
    private final String methodSignature;
    private final String[] methodeExceptions;

    private boolean isMethodPresent;

    public ClassAddMethodVisitor(int api, ClassVisitor cv, int methodAccess, String methodName, String methodDesc,
            String methodSignature, String[] methodeExceptions) {
        super(api, cv);
        this.methodAccess = methodAccess;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.methodSignature = methodSignature;
        this.methodeExceptions = methodeExceptions;
        isMethodPresent = false;
    }

    //同visitFiled一样 在visitMethod中进行标记
    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        if (name.equals(methodName) && descriptor.equals(methodDesc)) {
            isMethodPresent = true;
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }

    //在visitEnd中进行添加方法的操作 保证不会添加重复的方法
    //在子类中实现generateMethodBody方法 生成方法体
    public void visitEnd() {
        if (!isMethodPresent) {
            MethodVisitor mv = super.visitMethod(methodAccess, methodName, methodDesc, methodSignature,
                    methodeExceptions);
            if (mv != null) {
                generateMethodBody(mv);
                mv.visitEnd();
            }
        }
        super.visitEnd();
    }

    abstract void generateMethodBody(MethodVisitor mv);
}
