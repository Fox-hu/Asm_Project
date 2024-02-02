package com.fox.transform.remove_method;

/**
 * @Author fox
 * @Date 2024/2/3 0:10
 */
class HelloWorld_RemoveMethod {
    public int add(int a, int b) { //使用asm 删除 add 方法
        return a + b;
    }

    public int sub(int a, int b) {
        return a - b;
    }
}
