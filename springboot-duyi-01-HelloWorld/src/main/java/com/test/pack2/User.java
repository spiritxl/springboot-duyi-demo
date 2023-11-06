package com.test.pack2;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Data
@Accessors(chain = true)
public class User {
 
    /**
     * 真实姓名
     */
    @SecretColumn(strategy = SecretStrategy.USERNAME)
    private String realName;
 
    /**
     * 地址
     */
    @SecretColumn(strategy = SecretStrategy.ADDRESS)
    private String address;
 
    /**
     * 电话号码
     */
    @SecretColumn(strategy = SecretStrategy.PHONE)
    private String phoneNumber;
 
    /**
     * 身份证号码
     */
    @SecretColumn(strategy = SecretStrategy.ID_CARD)
    private String idCard;
 
}