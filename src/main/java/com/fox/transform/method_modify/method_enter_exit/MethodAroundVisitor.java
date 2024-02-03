package com.fox.transform.method_modify.method_enter_exit;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/***
 * 二合一的方式 在方法前后各打印一行日志
 * @Author fox
 * @Date 2024/2/3 21:26
 */
class MethodAroundVisitor extends ClassVisitor {
    public MethodAroundVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if(mv != null && !name.equals("<init>")){
            boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
            boolean isNative = (access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE;
            if(!isAbstract && !isNative){
                mv = new MethodAroundAdapter(api, mv);
            }
        }
        return mv;
    }

    private static class MethodAroundAdapter extends MethodVisitor{

        public MethodAroundAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitLdcInsn("Method Enter...");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if(opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)){
                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitLdcInsn("Method Exit...");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
