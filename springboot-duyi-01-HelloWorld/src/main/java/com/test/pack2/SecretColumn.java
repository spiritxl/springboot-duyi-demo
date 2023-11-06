package com.test.pack2;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 
 
@Target(ElementType.FIELD) // 标注在字段上
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside // 一般用于将其他的注解一起打包成"组合"注解
@JsonSerialize(using = SecretJsonSerializer.class) // 对标注注解的字段采用哪种序列化器进行序列化
public @interface SecretColumn {
 
    // 脱敏策略
    SecretStrategy strategy();
 
}