package com.fox.transform.method_modify.method_time_cost;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodTimerVisitor extends ClassVisitor {
    private String owner;
    private boolean isInterface;

    public MethodTimerVisitor(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        owner = name;
        isInterface = (access & Opcodes.ACC_INTERFACE) != 0;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if (!isInterface && mv != null && !"<init>".equals(name) && !"<clinit>".equals(name)) {
            boolean isAbstractMethod = (access & Opcodes.ACC_ABSTRACT) != 0;
            boolean isNativeMethod = (access & Opcodes.ACC_NATIVE) != 0;
            if (!isNativeMethod && !isAbstractMethod) {
                mv = new MethodTimerAdapter(api, mv, owner);
            }
        }
        return mv;
    }

    @Override
    public void visitEnd() {
        if (!isInterface) {
            FieldVisitor fv = super.visitField(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "timer", "J", null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }


    private static class MethodTimerAdapter extends MethodVisitor {
        private final String owner;

        public MethodTimerAdapter(int api, MethodVisitor mv, String owner) {
            super(api, mv);
            this.owner = owner;
        }

        @Override
        public void visitCode() {
            super.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMills", "()J", false);
            super.visitInsn(Opcodes.LSUB);
            super.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
            super.visitCode();
        }

        @Override
        public void visitInsn(int opcode) {
            if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
                super.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
                super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMills", "()J", false);
                super.visitInsn(Opcodes.LADD);
                super.visitFieldInsn(Opcodes.PUTSTATIC,owner,"timer","J");
            }
            super.visitInsn(opcode);
        }
    }
}
