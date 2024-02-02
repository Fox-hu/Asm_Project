package com.fox.transform.add_method;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * @Author fox
 * @Date 2024/2/2 23:58
 */
class AddMethodTest {

    public static void main(String[] args) {
        String path = "com/fox/transform/add_method/HelloWorld_AddMethod.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //创建 中间节点 添加目标方法
        ClassAddMethodVisitor cv = new ClassAddMethodVisitor(Opcodes.ASM9, cw, Opcodes.ACC_PUBLIC, "mul", "(II)I", null,
                null) {
            @Override
            void generateMethodBody(MethodVisitor mv) {
                mv.visitCode();
                mv.visitVarInsn(Opcodes.ILOAD, 1);
                mv.visitVarInsn(Opcodes.ILOAD, 2);
                mv.visitInsn(Opcodes.IMUL);
                mv.visitInsn(Opcodes.IRETURN);
                mv.visitMaxs(2, 3);
            }
        };
        int options = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, options);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
