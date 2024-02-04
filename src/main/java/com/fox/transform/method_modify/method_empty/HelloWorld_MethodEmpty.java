package com.fox.transform.method_modify.method_empty;

/**
 * @Author fox
 * @Date 2024/2/4 22:17
 */
class HelloWorld_MethodEmpty {

    //使用asm来晴空verify方法的方法体
    public void verify(String username, String password) throws IllegalArgumentException {
        if ("tomcat".equals(username) && "123456".equals(password)) {
            return;
        }
        throw new IllegalArgumentException("username or password is not correct");
    }
}
