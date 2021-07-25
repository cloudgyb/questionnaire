package com.gyb.questionnaire.util;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

/**
 * 加密工具类
 *
 * @author geng
 * 2020/11/5
 */
public class EncryptUtil {

    public static String encryptByAES(String content, String key) {
        if (key == null)
            throw new IllegalArgumentException("'key' is not allowed to be null!");
        try {
            final SecretKeySpec aesKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
            final byte[] bytes = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
            return Base64Utils.encodeToString(bytes);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException |
                InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptByAES(String password,String key){
        if (key == null)
            throw new IllegalArgumentException("'key' is not allowed to be null!");
        byte[] pass = Base64Utils.decodeFromString(password);
        try{
            final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8),"AES");
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
            final byte[] bytes = cipher.doFinal(pass);
            return new String(bytes,StandardCharsets.UTF_8);
        }
        catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encryptPassword(String pass, String passSalt) {
        final String base64Str = Base64Utils.encodeToString(pass.getBytes());
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(passSalt.getBytes());
            final byte[] digest = md5.digest(base64Str.getBytes());
            return byteToHexString(digest);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byteToHexString(byte[] b) {
        StringBuilder hexString = new StringBuilder();
        for (byte value : b) {
            String hex = Integer.toHexString(value & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        //final String s = encryptPassword("1234ddf@@@@%%%dfdfsdfdsf56", "2345");
        //System.out.println(s);
        String key="questionnaire@cloudgyb_19950627&";
        final String s = encryptByAES("root", key);
        System.out.println(s);
        final String s1 = decryptByAES(s, key);
        System.out.println(s1);
    }

}
