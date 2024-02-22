package com.test.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class YourClass {
    public static void main(String[] args) {

        CloseableHttpResponse response = null;

        // 设置URL
        String url = "http://172.19.100.102:18000";

        // 创建HttpClient实例
        HttpClient httpClient = HttpClients.createDefault();

        // 创建HttpPost请求
        HttpPost httpPost = new HttpPost(url);

        // 设置JSON参数
        String jsonParams = "{\n" +
                "    \"command\": \"stat-lease4-get\",\n" +
                "    \"service\": [\n" +
                "        \"dhcp4\"\n" +
                "    ],\n" +
                "    \"arguments\": {\n" +
                "        \"ip-address\": \"172.19.100.222\"\n" +
                "    }\n" +
                "}";

        StringEntity entity = new StringEntity(jsonParams, "UTF-8");

        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        System.out.println(jsonParams);
        try {
            // 发送POST请求
            response = (CloseableHttpResponse) httpClient.execute(httpPost);
            System.out.println("---"+entity.getContent().toString());
            // 处理响应
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String responseBody = EntityUtils.toString(response.getEntity(), "UTF-8");
                response.close();
                if (!responseBody.isEmpty()) {
                    // 解析响应JSON并转换为YourClass类型
                    ObjectMapper objectMapper = new ObjectMapper();
                    HashMap<String,Object> responseObject = (HashMap<String, Object>) objectMapper.readValue(responseBody, Map.class);
                    // 现在你可以使用得到的对象进行操作
                    System.out.println(responseObject.toString());
                } else {
                    System.out.println("Empty response body");
                }
            } else {
                System.err.println("HTTP POST request failed with status code: " + statusCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
