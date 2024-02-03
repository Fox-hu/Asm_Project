package com.fox.transform.method_modify.method_enter_exit;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 在方法前加入一个方法
 * @Author fox
 * @Date 2024/2/3 12:13
 */
class MethodEnterVisitor extends ClassVisitor {
    public MethodEnterVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        //当方法是构造方法时 不进行代码织入
        if (mv != null && !name.equals("<init>")) {
            mv = new MethodEnterAdapter(api, mv);
        }
        return mv;
    }

    private static class MethodEnterAdapter extends MethodVisitor {
        public MethodEnterAdapter(int api, MethodVisitor mv) {
            super(api, mv);
        }

        @Override
        public void visitCode() {
            //jvm执行方法的顺序是
            //1 创建对象
            //2 创建参数
            //3 调用INVOKEVIRTUAL\INVOKESTATIC\等方法
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("Method Enter...");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V",
                    false);
            //添加完代码后 调用super.visitCode()保证方法正常执行
            super.visitCode();
        }
    }
}
