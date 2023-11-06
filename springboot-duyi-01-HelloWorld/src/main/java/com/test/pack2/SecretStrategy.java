package com.test.pack2;

import lombok.Getter;

import java.util.function.Function;

/**
 * 脱敏策略，不同数据可选择不同的策略
 */
@Getter
public enum SecretStrategy {
 
    /**
     * 用户名脱敏
     */
    USERNAME(str -> str.replaceAll("(\\S)\\S(\\S*)", "$1*$2")),
 
    /**
     * 身份证脱敏
     */
    ID_CARD(str -> str.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2")),
 
    /**
     * 手机号脱敏
     */
    PHONE(str -> str.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2")),
 
    /**
     * 地址脱敏
     */
    ADDRESS(str -> str.replaceAll("(\\S{3})\\S{2}(\\S*)\\S{2}", "$1****$2****"));
 
    private final Function<String, String> desensitizer;
 
    SecretStrategy(Function<String, String> desensitizer){
        this.desensitizer = desensitizer;
    }
 
 
 
}