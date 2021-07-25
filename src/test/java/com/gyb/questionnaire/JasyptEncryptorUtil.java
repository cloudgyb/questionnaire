package com.gyb.questionnaire;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author cloudgyb
 * 2021/7/25 9:48
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class JasyptEncryptorUtil {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encryptDatabaseProperties() {
        final String userName = stringEncryptor.encrypt("root");
        System.out.println(userName);
        final String password = stringEncryptor.encrypt("123456");
        System.out.println(password);
    }

}
