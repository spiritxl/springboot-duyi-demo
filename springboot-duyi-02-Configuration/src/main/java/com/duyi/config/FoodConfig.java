package com.duyi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * food:
 *   meet: 烤肉
 *   rice: 拌饭
 * @Configuration
 *      声明这是一个配置类 可以代替spring.xml
 * @ConfigurationProperties(prefix = "food")
 *      声明这是一个配置文件类
 * @PropertySource("classpath:food.yml")
 *      声明文件对应的地址
 */
@Configuration
@ConfigurationProperties(prefix = "food")
@PropertySource("classpath:application.yml")
public class FoodConfig {

    private String meet;
    private String rice;


    public String getMeet() {
        return meet;
    }

    public void setMeet(String meet) {
        this.meet = meet;
    }

    public String getRice() {
        return rice;
    }

    public void setRice(String rice) {
        this.rice = rice;
    }
}
