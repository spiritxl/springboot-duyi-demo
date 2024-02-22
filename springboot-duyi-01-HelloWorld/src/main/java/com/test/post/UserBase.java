package com.test.post;

import java.util.Date;

public class UserBase {
 
    /**
     * 用户名
     */
    private String userName;
 
 
    /**
     * 年龄
     */
    private Integer age;
 
 
    /**
     * 增加时间
     */
    private Date addTime;
 
    public String getUserName() {
        return userName;
    }
 
    public void setUserName(String userName) {
        this.userName = userName;
    }
 
    public Integer getAge() {
        return age;
    }
 
    public void setAge(Integer age) {
        this.age = age;
    }
 
    public Date getAddTime() {
        return addTime;
    }
 
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}