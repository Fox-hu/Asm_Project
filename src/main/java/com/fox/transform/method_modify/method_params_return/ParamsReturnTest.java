package com.fox.transform.method_modify.method_params_return;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

/**
 * @Author fox
 * @Date 2024/2/3 22:53
 */
class ParamsReturnTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/method_modify/method_params_return/HelloWorld_ParamsReturn.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor cv = new MethodParamsReturnVisitor(Opcodes.ASM9, cw);
        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, option);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
