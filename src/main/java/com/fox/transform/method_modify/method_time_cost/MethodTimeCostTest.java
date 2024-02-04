package com.fox.transform.method_modify.method_time_cost;

import com.fox.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class MethodTimeCostTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/method_modify/method_time_cost/HelloWorld_TimeCost.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
//      两种方式进行代码耗时计算
//        ClassVisitor cv = new MethodTimerVisitor(Opcodes.ASM9, cw);
        ClassVisitor cv = new MethodTimerVisitor2(Opcodes.ASM9, cw);
        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, option);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
