package com.fox.transform.remove_filed;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;

public class ClassRemoveFieldVisitor extends ClassVisitor {
    private final String filedName;
    private final String filedDesc;

    public ClassRemoveFieldVisitor(int api, ClassVisitor cv, String filedName, String filedDesc) {
        super(api, cv);
        this.filedName = filedName;
        this.filedDesc = filedDesc;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        //对于想要删除的字段 返回一个null值即可
        if (name.equals(filedName) && descriptor.equals(filedDesc)) {
            return null;
        } else {
            return super.visitField(access, name, descriptor, signature, value);
        }
    }
}
