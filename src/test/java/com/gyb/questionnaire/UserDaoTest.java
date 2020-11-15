package com.gyb.questionnaire;

import com.gyb.questionnaire.dao.UserDao;
import com.gyb.questionnaire.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author geng
 * 2020/11/5
 */

@SpringBootTest
public class UserDaoTest {

    @Resource
    private UserDao userDao;

    @Test
    public void testFindAll(){
        final List<User> all = userDao.findAll();
        Assert.isTrue(!all.isEmpty(),"");
    }
    @Test
    public void testFindByUsername(){
        final List<User> all = userDao.findByUsername("geng");
        Assert.isTrue(!all.isEmpty(),"");
    }
    @Test
    public void testCountByUsername(){
        final int i = userDao.countByUsername("");
        Assert.isTrue(i==0,"");
    }
}
