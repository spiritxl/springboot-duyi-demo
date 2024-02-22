package com.test.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMain {
 
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json1 = "{\"userName\":\"小李飞刀\",\"age\":18,\"addTime\":1591851786568}";
        String json2 = "[{\"userName\":\"小李飞刀\",\"age\":18,\"addTime\":123}, {\"userName\":\"小李飞刀2\",\"age\":182,\"addTime\":1234}]";
        try {
            //1、json字符串转为对象
            UserBase userBase1 = objectMapper.readValue(json1, UserBase.class);
            UserBase userBase2 = objectMapper.readValue(json1, new TypeReference<UserBase>(){});
            System.out.println(userBase1.getUserName());
            System.out.println(userBase2.getUserName());
 
            //2、json转为map
            Map<String,Object> map = objectMapper.readValue(json1, new TypeReference<Map<String,Object>>(){});
            //map:{userName=小李飞刀, age=18, addTime=1591851786568}
            System.out.println("map:" + map);
 
            //3、json转为list<bean>
            //使用json1报错，此时需要数组/集合类型： Can not deserialize instance of TEst.UserBase[] out of START_OBJECT token
            List<UserBase> lists = objectMapper.readValue(json2, new TypeReference<List<UserBase>>(){});
            System.out.println(lists.get(0).getUserName());
 
            //4、json转为数组
            UserBase[] userBases = objectMapper.readValue(json1, new TypeReference<UserBase[]>(){});
            System.out.println(userBases[0].getUserName());
 
 
            //序列化
            Map<String,String> map1 = new HashMap<>();
            map1.put("name", "小李飞刀");
            map1.put("sex", "男");
            String json = objectMapper.writeValueAsString(map1);
            System.out.println(json);
            //反序列化
            Map<String,String> maps = objectMapper.readValue(json,new TypeReference<Map<String,String>>(){});
            System.out.println(maps.get("name"));
            //反序列化
            List<UserBase> listes = objectMapper.readValue(json,new TypeReference<List<UserBase>>(){});
            System.out.println(listes.get(0).getUserName());
            //反序列化
            UserBase[] userBasess = objectMapper.readValue(json,new TypeReference<UserBase[]>(){});
            System.out.println(userBasess[0].getUserName());
 
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            //法一
            List<UserBase> userBases1 = objectMapper.readValue(json2, new TypeReference<List<UserBase>>(){});
            //法二
            JavaType javaType= objectMapper.getTypeFactory().constructCollectionType(List.class, UserBase.class);
            List<UserBase> list2 = objectMapper.readValue(json1, javaType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}