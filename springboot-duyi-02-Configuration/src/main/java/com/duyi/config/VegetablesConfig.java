package com.duyi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


/**
 * vegetable:
 *   potato: 土豆
 *   eggplant: 茄子
 *   greenpeper: 青椒
 * @Configuration
 *      声明这是一个配置类 可以代替spring.xml
 * @ConfigurationProperties(prefix = "vegetable")
 *      声明这是一个配置文件类
 * @PropertySource("classpath:application.yml")
 *      声明文件对应的地址
 */
@Configuration
@ConfigurationProperties(prefix = "vegetables")
@PropertySource("classpath:application.properties")
public class VegetablesConfig {


    private String potato;


    private String eggplant;


    private String greenpeper;

    public String getPotato() {
        return potato;
    }

    public void setPotato(String potato) {
        this.potato = potato;
    }

    public String getEggplant() {
        return eggplant;
    }

    public void setEggplant(String eggplant) {
        this.eggplant = eggplant;
    }

    public String getGreenpeper() {
        return greenpeper;
    }

    public void setGreenpeper(String greenpeper) {
        this.greenpeper = greenpeper;
    }

}
