package com.test.aes;




import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

import java.nio.charset.StandardCharsets;

public class AesUtil {

    /**
     * AES 解密数据
     * @param data 需加密的数据
     * @param aesKey aes密钥
     * @return 加密后的数据
     */
    public static String decryptAESData(String data,String aesKey) {
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey.getBytes(StandardCharsets.UTF_8));
        //解密为字符串  返回明文密码
        return aes.decryptStr(data, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * AES 加密数据
     * @param data 需加密的数据
     * @param aesKey aes密钥
     * @return 加密后的数据
     */
    public static String aesEncryptData(String data,String aesKey){
        // 设置AES加密
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, aesKey.getBytes(StandardCharsets.UTF_8));
        return aes.encryptHex(data);
    }
}
