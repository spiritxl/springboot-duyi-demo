package com.test.post;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class MineClass {
    public static void main(String[] args) {

        RestTemplate restTemplate = new RestTemplate();

        CloseableHttpResponse response = null;

        // 设置URL
        String url = "http://localhost:8080/aaa";

//        // 创建HttpClient实例
//        HttpClient httpClient = HttpClients.createDefault();
//
//        // 创建HttpPost请求
//        HttpPost httpPost = new HttpPost(url);

//        // 设置JSON参数
//        String jsonParams = "{\n" +
//                "    \"name\": \"lsz\"\n" +
//                "}";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> mulMap = new LinkedMultiValueMap<>();
        Map<String,String> map = new HashMap();
        map.put("Name","name");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            mulMap.add(entry.getKey(), entry.getValue());
        }
        //将请求头和请求参数设置到HttpEntity中
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<>(mulMap, httpHeaders);
        People people = (People) restTemplate.postForObject(url, httpEntity, People.class);
        System.out.println(people);
    }
}
