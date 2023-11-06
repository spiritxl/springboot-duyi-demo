package com.test.pack2;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.util.Objects;

/**
 * 序列化器实现
 */
public class SecretJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {
 
    private SecretStrategy secretStrategy;
 
    /**
     * 步骤一
     * 方法来源于ContextualSerializer，获取属性上的注解属性,同时返回一个合适的序列化器
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        // 获取自定义注解
        SecretColumn annotation = beanProperty.getAnnotation(SecretColumn.class);
        // 注解不为空，且标注的字段为String
        if(Objects.nonNull(annotation) && Objects.equals(String.class, beanProperty.getType().getRawClass())){
            this.secretStrategy = annotation.strategy();
            // 符合我们自定义情况，返回本序列化器，将顺利进入到该类中的serialize（）方法中
            return this;
        }
        // 注解为空，字段不为String，寻找合适的序列化器进行处理
        return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
    }
 
    /**
     * 步骤二
     * 方法来源于JsonSerializer<String>：指定返回类型为String类型，serialize()将修改后的数据返回
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if(Objects.isNull(secretStrategy)){
            // 定义策略为空，返回原字符串
            jsonGenerator.writeString(s);
        }else {
            // 定义策略不为空，返回策略处理过的字符串
            jsonGenerator.writeString(secretStrategy.getDesensitizer().apply(s));
        }
    }
}