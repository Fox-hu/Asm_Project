package com.fox.transform.method_modify.method_time_cost;

import java.util.Random;

//使用asm 计算方法耗时
public class HelloWorld_TimeCost {
    public int add(int a, int b) throws InterruptedException {
        int c = a + b;
        Random rand = new Random(System.currentTimeMillis());
        int num = rand.nextInt(300);
        Thread.sleep(100 + num);
        return c;
    }

    public int sub(int a, int b) throws InterruptedException {
        int c = a - b;
        Random rand = new Random(System.currentTimeMillis());
        int num = rand.nextInt(400);
        Thread.sleep(100 + num);
        return c;
    }
}
