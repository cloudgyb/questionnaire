package com.gyb.questionnaire.util;

import org.springframework.util.Base64Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 *
 * @author geng
 * 2020/11/5
 */
public class EncryptUtil {
    public static String encryptPassword(String pass,String passSalt) {
        final String base64Str = Base64Utils.encodeToString(pass.getBytes());
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(passSalt.getBytes());
            final byte[] digest = md5.digest(base64Str.getBytes());
            return byteToHexString(digest);
        } catch (NoSuchAlgorithmException e) {
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
        final String s = encryptPassword("1234ddf@@@@%%%dfdfsdfdsf56","2345");
        System.out.println(s);
    }

}
