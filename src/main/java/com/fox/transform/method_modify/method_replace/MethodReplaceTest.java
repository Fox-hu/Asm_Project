package com.fox.transform.method_modify.method_replace;

import com.fox.FileUtils;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;

/**
 * @Author fox
 * @Date 2024/2/4 23:32
 */
class MethodReplaceTest {

    public static void main(String[] args) {
        String path = "com/fox/transform/method_modify/method_replace/HelloWorld_MethodReplace.class";
        String filePath = FileUtils.getFilePath(path);
        byte[] bytes1 = FileUtils.readBytes(filePath);

        ClassReader cr = new ClassReader(bytes1);
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        // 使用asm替换静态方法 Math.max() 方法。
        ClassVisitor cv1 = new MethodReplaceInvokeVisitor(ASM9, cw, "java/lang/Math", "max", "(II)I", INVOKESTATIC,
                "java/lang/Math", "min", "(II)I");

        // 使用asm替换非静态方法 PrintStream.println() 方法。
        String newOwner = "com/fox/transform/method_modify/method_replace/ParameterUtils";
        ClassVisitor cv2 = new MethodReplaceInvokeVisitor(ASM9, cv1, "java/io/PrintStream", "println", "(I)V", INVOKESTATIC,
                newOwner, "output", "(Ljava/io/PrintStream;I)V");
        int option = ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES;
        cr.accept(cv2, option);

        byte[] bytes2 = cw.toByteArray();
        FileUtils.writeBytes(filePath, bytes2);
    }
}
