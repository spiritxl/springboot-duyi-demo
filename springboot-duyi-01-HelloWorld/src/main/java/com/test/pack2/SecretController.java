package com.test.pack2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secret")
public class SecretController {

    @GetMapping("/test")
    public User test(){
        User user = new User();
        user.setRealName("陈平安")
                .setPhoneNumber("12345678910")
                .setAddress("浩然天下宝瓶洲骊珠洞天泥瓶巷")
                .setIdCard("4493888665464654659");
        System.out.println(user);
        return user;
    }

}