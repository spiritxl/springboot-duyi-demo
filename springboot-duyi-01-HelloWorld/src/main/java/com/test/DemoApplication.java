package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        //使用SpringApplication类的静态方法  启动springboot程序
        //方法的参数  程序的入口类  main函数的参数
        SpringApplication.run(DemoApplication.class,args);
    }
}
