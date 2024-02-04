package com.fox.transform.method_modify.method_replace;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;

/**
 * @Author fox
 * @Date 2024/2/4 23:23
 */
class MethodReplaceInvokeVisitor extends ClassVisitor {

    private final String oldOwner;
    private final String oldMethodName;
    private final String oldMethodDesc;

    private final int newOpcode;
    private final String newOwner;
    private final String newMethodName;
    private final String newMethodDesc;

    public MethodReplaceInvokeVisitor(int api, ClassVisitor classVisitor, String oldOwner, String oldMethodName,
            String oldMethodDesc, int newOpcode, String newOwner, String newMethodName, String newMethodDesc) {
        super(api, classVisitor);
        this.oldOwner = oldOwner;
        this.oldMethodName = oldMethodName;
        this.oldMethodDesc = oldMethodDesc;

        this.newOpcode = newOpcode;
        this.newOwner = newOwner;
        this.newMethodName = newMethodName;
        this.newMethodDesc = newMethodDesc;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
            boolean isAbstract = (access & ACC_ABSTRACT) == ACC_ABSTRACT;
            boolean isNative = (access & ACC_NATIVE) == ACC_NATIVE;
            if (!isAbstract && !isNative) {
                mv = new MethodReplaceInvokeAdapter(api, mv);
            }
        }
        return mv;
    }

    private class MethodReplaceInvokeAdapter extends MethodVisitor {

        public MethodReplaceInvokeAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        //1.在替换 static 方法的时候，要保证一点：替换方法前，和替换方法后，要保持“方法接收的参数”和“方法的返回类型”是一致的。
        //2.对于 non-static 方法来说，它有一个隐藏的 this 变量。我们在替换 non-static 方法的时候，要把 this 变量给“消耗”掉。
        //所以最佳实践是将non-static方法替换成static方法 切这个static方法第一个参数为这个non-static方法的this参数。
        // 比如 System.out.println(int:val) 方法 替换成

        // public class ParameterUtils {
        //    public static void output(PrintStream printStream, int val) {
        //        printStream.println("ParameterUtils: " + val);
        //    }
        //}。
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            if (oldMethodName.equals(name) && oldOwner.equals(owner) && oldMethodDesc.equals(descriptor)) {
                super.visitMethodInsn(newOpcode, newOwner, newMethodName, newMethodDesc, false);
            } else {
                super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
            }
        }
    }
}

