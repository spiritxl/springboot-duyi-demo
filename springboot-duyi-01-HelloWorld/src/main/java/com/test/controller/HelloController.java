package com.test.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {

    /**
     * @RequestMapping
     * 指定方法和请求之间的映射关系
     * @return
     */
    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "Hello DuYi";
    }
}
