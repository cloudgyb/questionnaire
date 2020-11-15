package com.gyb.questionnaire;

import com.gyb.questionnaire.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionnaireApplicationTests {

    @Autowired
    private UserDao userDao;

}
