package com.fox.transform.method_modify.method_enter_exit;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @Author fox
 * @Date 2024/2/3 20:52
 */
class MethodEnterExitTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/method_modify/method_enter_exit/HelloWorld_MethodEnterExit.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //第一种方式 串联两个visitor实现
//        ClassVisitor cv_exit = new MethodExitVisitor(Opcodes.ASM9, cw);
//        ClassVisitor cv_enter = new MethodEnterVisitor(Opcodes.ASM9, cv_exit);
//
//        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
//        cr.accept(cv_enter, option);

        //第二种方式 通过一个visitor实现
        ClassVisitor cv = new MethodAroundVisitor(Opcodes.ASM9, cw);
        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, option);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}

