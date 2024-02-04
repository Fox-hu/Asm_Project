package com.fox.transform.method_modify.method_empty;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;

/**
 * @Author fox
 * @Date 2024/2/4 22:35
 */
class MethodEmptyTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/method_modify/method_empty/HelloWorld_MethodEmpty.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor cv = new MethodEmptyBodyVisitor(ASM9, cw, "verify", "(Ljava/lang/String;Ljava/lang/String;)V");

        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, option);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
