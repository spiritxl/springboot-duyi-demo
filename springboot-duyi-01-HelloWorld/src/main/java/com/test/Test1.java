package com.test;


import com.test.config.SpringConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

public class Test1 {

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(SpringConfig.class);
//        Object controller = context.getBean("controller");
//        System.out.println(controller);
        String s ="";
        System.out.println(s==null);
        System.out.println(s.equals(""));
    }
}
