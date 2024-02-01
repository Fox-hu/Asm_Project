package com.fox.transform.remove_filed;

import com.fox.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public class RemoveFiledTest {
    public static void main(String[] args) {

        String path = "com/fox/transform/remove_filed/HelloWorld_RemoveFiled.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);
        //创建 ClassReader 和 ClassWriter 链条的起点和终点
        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        //创建 中间节点 删除目标字段
        int api = Opcodes.ASM9;
        ClassVisitor cv = new ClassRemoveFieldVisitor(api, cw, "strValue", "Ljava/lang/String;");

        int parsingOptions = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        //链接起始、中间、尾部
        cr.accept(cv, parsingOptions);

        //重新输出处理后的class
        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
