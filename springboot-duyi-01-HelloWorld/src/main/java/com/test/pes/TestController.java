package com.test.pes;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.Key;
import java.util.Random;

public class TestController {

    public static void main(String[] args) {
        String userName = "lvsizhao";
        String password = "123456";
        //获取8位的盐值
        String salt = randomGen(8);
        System.out.println(encrypt(userName,password,salt));
    }


    public static String randomGen(int place) {
        String base = "qwertyuioplkjhgfdsazxcvbnmQAZWSXEDCRFVTGBYHNUJMIKLOP0123456789";
        StringBuffer sb = new StringBuffer();
        Random rd = new Random();
        for(int i=0;i<place;i++) {
            sb.append(base.charAt(rd.nextInt(base.length())));
        }
        return sb.toString();
    }



    public static String encrypt(String plaintext, String password, String salt) {
        //根据密码获取派生出来的密钥
        Key key = getPbeKey(password);
        byte[] encipheredData = null;
        //指定了加密时使用的盐（salt）和迭代次数。
        PBEParameterSpec parameterSpec = new PBEParameterSpec(salt.getBytes(), 1000);
        try {
            //创建了一个用于加密和解密的 Cipher 对象，并指定了加密算法为 PBEWithMD5AndDES。
            Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

            //MODE -> 加密操作
            //key->密钥
            //parameterSpec ->执行加密操作所需的参数
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            encipheredData = cipher.doFinal(plaintext.getBytes("utf-8"));
        } catch (Exception e) {
        }
        //将字节数组转化为16进制
        return bytesToHexString(encipheredData);
    }


    private static Key getPbeKey(String password) {
        // 实例化使用的算法
        SecretKeyFactory keyFactory;
        SecretKey secretKey = null;
        try {
            keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            // 设置PBE密钥参数
            PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
            // 生成密钥
            secretKey = keyFactory.generateSecret(keySpec);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return secretKey;
    }


    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }



}
