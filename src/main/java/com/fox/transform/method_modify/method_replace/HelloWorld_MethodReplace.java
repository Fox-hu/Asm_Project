package com.fox.transform.method_modify.method_replace;

/**
 * @Author fox
 * @Date 2024/2/4 23:20
 */
class HelloWorld_MethodReplace {
    //使用asm替换静态方法 Math.max() 方法。
    //使用asm替换非静态方法 PrintStream.println() 方法。
    public void test(int a, int b) {
        int c = Math.max(a, b);
        System.out.println(c);
    }
}
