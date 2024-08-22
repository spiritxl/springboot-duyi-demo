package com.test;


import com.test.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;

public class Test1 {

    public static Long aLong = 1L;

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(SpringConfig.class);
//        Object controller = context.getBean("controller");
//        System.out.println(controller);

        C();
    }

    public static void A(){
        Long a = aLong;
        a++;
        System.out.println(a);
    }

    public static void B(){
        System.out.println(aLong);
    }

    public static void C(){
        Lock lock = new Lock();
        boolean status = lock.isStatus();
        lock.setStatus(!status);
    }
}
