package com.fox.transform.method_modify.method_empty;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import static org.objectweb.asm.Opcodes.*;

/**
 * 演示如何清空一个方法体
 * @Author fox
 * @Date 2024/2/4 22:19
 */
class MethodEmptyBodyVisitor extends ClassVisitor {
    private String owner;
    private final String methodName;
    private final String methodDesc;

    public MethodEmptyBodyVisitor(int api, ClassVisitor classVisitor, String methodName, String methodDesc) {
        super(api, classVisitor);
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.owner = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && name.equals(methodName) && descriptor.equals(methodDesc)) {
            boolean isAbstract = (access & ACC_ABSTRACT) == ACC_ABSTRACT;
            boolean isNative = (access & ACC_NATIVE) == ACC_NATIVE;
            if (!isAbstract && !isNative) {
                generateNewBody(mv, owner, access, name, descriptor);
            }
        }
        return mv;
    }

    private void generateNewBody(MethodVisitor mv, String owner, int access, String name, String descriptor) {
        //获取方法的参数和返回值类型
        Type type = Type.getType(methodDesc);
        Type[] argumentTypes = type.getArgumentTypes();
        Type returnType = type.getReturnType();

        //计算局部变量表的大小和操作数栈的大小
        boolean isStatic = (access & ACC_STATIC) != 0;
        int localSize = isStatic ? 0 : 1;
        for (Type argumentType : argumentTypes) {
            localSize += argumentType.getSize();
        }
        int stackSize = returnType.getSize();

        //生成方法体
        mv.visitCode();
        if (returnType.getSort() == Type.VOID) {
            mv.visitInsn(RETURN);
        } else if (returnType.getSort() >= Type.BOOLEAN && returnType.getSort() <= Type.INT) {
            mv.visitInsn(ICONST_0);
            mv.visitInsn(IRETURN);
        } else if (returnType.getSort() == Type.LONG) {
            mv.visitInsn(LCONST_0);
            mv.visitInsn(LRETURN);
        } else if (returnType.getSort() == Type.FLOAT) {
            mv.visitInsn(FCONST_0);
            mv.visitInsn(FRETURN);
        } else if (returnType.getSort() == Type.DOUBLE) {
            mv.visitInsn(DCONST_0);
            mv.visitInsn(DRETURN);
        } else {
            mv.visitInsn(ACONST_NULL);
            mv.visitInsn(ARETURN);
        }
        mv.visitMaxs(stackSize, localSize);
        mv.visitEnd();
    }
}
