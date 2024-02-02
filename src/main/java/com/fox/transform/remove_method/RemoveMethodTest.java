package com.fox.transform.remove_method;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @Author fox
 * @Date 2024/2/3 0:13
 */
class RemoveMethodTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/remove_method/HelloWorld_RemoveMethod.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor cv = new ClassRemoveMethodVisitor(Opcodes.ASM9, cw, "add", "(II)I");

        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, parsingOptions);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
