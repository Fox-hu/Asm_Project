package com.fox.transform.add_filed;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;


/**
 * @Author fox
 * @Date 2024/2/1 23:50
 */
class AddFiledTest {
    public static void main(String[] args) {
        String path = "com/fox/transform/add_filed/HelloWorld_AddFiled.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassAddFiledVisitor cv = new ClassAddFiledVisitor(Opcodes.ASM9, cw, Opcodes.ACC_PUBLIC, "objValue",
                "Ljava/lang/Object;");

        int options = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv, options);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
