package com.fox.transform.add_filed;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

/**
 * @Author fox
 * @Date 2024/2/1 23:38
 */
class ClassAddFiledVisitor extends ClassVisitor {

    private final int filedAccess;
    private final String filedName;
    private final String filedDesc;
    private boolean isFiledPresent;

    public ClassAddFiledVisitor(int api, ClassVisitor classVisitor, int filedAccess, String filedName,
            String filedDesc) {
        super(api, classVisitor);
        this.filedAccess = filedAccess;
        this.filedName = filedName;
        this.filedDesc = filedDesc;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        //并不是在visitField方法中进行添加字段的操作
        // 如果已经有了该新增字段 则标记 也仅仅只是进行了一个标记
        if (name.equals(filedName)) {
            isFiledPresent = true;
        }
        return super.visitField(access, name, descriptor, signature, value);
    }

    //最终是在visitEnd方法中进行添加字段的操作
    //因为在visitFiled中进行操作 则有可能添加重复的字段
    @Override
    public void visitEnd() {
        //当前文件全部字段都访问过了 没有要添加的字段 此时进行字段添加操作
        //注意 此时使用的是super.visitFiled方法 而不是this.visitFiled方法
        if (!isFiledPresent) {
            FieldVisitor fv = super.visitField(filedAccess, filedName, filedDesc, null, null);
            if (fv != null) {
                fv.visitEnd();
            }
        }
        super.visitEnd();
    }
}
