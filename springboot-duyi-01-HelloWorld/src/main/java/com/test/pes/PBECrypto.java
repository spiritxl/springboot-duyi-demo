package com.test.pes;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class PBECrypto {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 10000; // 迭代次数，可以根据需要调整
    private static final int KEY_LENGTH = 256; // 密钥长度，可以根据需要调整

    public static String encryptPassword(String password, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verifyPassword(String password, String salt, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String newHashedPassword = encryptPassword(password, salt);
        return newHashedPassword.equals(hashedPassword);
    }

    public static void main(String[] args) {
        String password = "123456";
        String salt = "lvsizhao";

        try {
            String hashedPassword = PBECrypto.encryptPassword(password, salt);
            System.out.println("Hashed Password: " + hashedPassword);

            boolean isPasswordCorrect = PBECrypto.verifyPassword(password, salt, hashedPassword);
            System.out.println("Password verification result: " + isPasswordCorrect);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
