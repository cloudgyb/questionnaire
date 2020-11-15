package com.gyb.questionnaire;

import com.gyb.questionnaire.dao.TemplateDao;
import com.gyb.questionnaire.entity.Template;
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
public class QuestionnaireTemplateTest {

    @Resource
    private TemplateDao dao;

    @Test
    public void testFindAll() {
        final List<Template> all = dao.findAll();
        System.out.println("fdfdf");
        Assert.isTrue(!all.isEmpty(), "");
    }

    @Test
    public void testFindByTypeId() {
        final List<Template> all = dao.findByTypeId(1);
        System.out.println("fdfdf");
        Assert.isTrue(!all.isEmpty(), "");
    }
//
//    @Test
//    public void testCountByUsername() {
//        final int i = userDao.countByUsername("");
//        Assert.isTrue(i == 0, "");
//    }
}
