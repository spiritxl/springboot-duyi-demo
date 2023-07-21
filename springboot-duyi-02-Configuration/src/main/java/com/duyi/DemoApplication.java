package com.duyi;


import com.duyi.config.FoodConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @SpringBootApplication
 *      这是spring boot的入口类 声明这是一个springboot项目
 * @EnableConfigurationProperties({VegetablesConfig.class})
 *      告诉主程序入口类 要自动引入配置文件  配置文件对应的类作为它的参数
 */
@SpringBootApplication
@EnableConfigurationProperties({FoodConfig.class})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }
}
