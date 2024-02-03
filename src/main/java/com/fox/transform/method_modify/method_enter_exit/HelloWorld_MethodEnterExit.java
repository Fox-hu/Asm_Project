package com.fox.transform.method_modify.method_enter_exit;

/**
 * @Author fox
 * @Date 2024/2/3 12:09
 */
class HelloWorld_MethodEnterExit {
    public void test() {
        //System.out.println("Method Enter..."); 在方法调用前 用asm添加这行代码
        System.out.println("this is a test method.");
        //System.out.println("Method Exit..."); 在方法调用后 用asm添加这行代码
    }
}
