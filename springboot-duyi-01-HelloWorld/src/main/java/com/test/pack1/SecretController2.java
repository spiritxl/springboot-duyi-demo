package com.test.pack1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secret")
public class SecretController2 {

    @GetMapping("/test2")
    public SysUser test(){
        SysUser user = new SysUser();
        user.setUserName("吕思钊")
            .setPhoneNumber("18855666532")
            .setEmail("3243388324@qq.com");
        System.out.println(user);
        return user;
    }

}