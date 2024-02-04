package com.fox.transform.method_modify.method_params_return;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/***
 * 打印方法的参数和返回值 并不灵活
 * @Author fox
 * @Date 2024/2/3 22:41
 */
class MethodParamsReturnVisitor extends ClassVisitor {
    public MethodParamsReturnVisitor(int api, ClassVisitor cv) {
        super(api, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
            String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (mv != null && !name.equals("<init>")) {
            boolean isAbstract = (access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT;
            boolean isNative = (access & Opcodes.ACC_NATIVE) == Opcodes.ACC_NATIVE;
            if (!isAbstract && !isNative) {
                mv = new MethodParamsReturnAdapter(api, mv);
            }

        }
        return mv;
    }

    private static class MethodParamsReturnAdapter extends MethodVisitor{

        public MethodParamsReturnAdapter(int api, MethodVisitor methodVisitor) {
            super(api, methodVisitor);
        }

        @Override
        public void visitCode() {
            //方法执行前的逻辑
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V",false);

            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(Opcodes.ILOAD, 2);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(I)V",false);

            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(Opcodes.LLOAD, 3);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(J)V",false);

            //因为index为3的参数为long型 占了两个slot 所以这个参数从5开始
            super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            super.visitVarInsn(Opcodes.ALOAD, 5);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/Object;)V",false);

            //处理之后的逻辑
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if(opcode == Opcodes.ATHROW || (opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)){
                //方法执行后的逻辑
                super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                super.visitVarInsn(Opcodes.ILOAD, 6);
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            }
            super.visitInsn(opcode);
        }
    }
}
