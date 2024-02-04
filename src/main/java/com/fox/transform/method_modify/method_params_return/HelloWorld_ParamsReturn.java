package com.fox.transform.method_modify.method_params_return;

/**
 * @Author fox
 * @Date 2024/2/3 22:40
 */
class HelloWorld_ParamsReturn {
    //使用asm打印出方法的参数和返回值
    public int test(String name, int age, long idCard, Object obj) {
        int hashCode = 0;
        hashCode += name.hashCode();
        hashCode += age;
        hashCode += (int) (idCard % Integer.MAX_VALUE);
        hashCode += obj.hashCode();
        return hashCode;
    }
}
