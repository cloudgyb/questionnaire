package com.gyb.questionnaire;

import com.gyb.questionnaire.dao.ESTemplateRepository;
import com.gyb.questionnaire.dao.TemplateQuestionDao;
import com.gyb.questionnaire.entity.ESTemplate;
import com.gyb.questionnaire.entity.ESTemplateQuestion;
import com.gyb.questionnaire.entity.Template;
import com.gyb.questionnaire.entity.TemplateQuestion;
import com.gyb.questionnaire.service.TemplateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author geng
 * 2020/11/5
 */

@SpringBootTest
public class ESTemplateSearchTest {
    @Autowired
    private ESTemplateRepository templateRepository;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TemplateQuestionDao templateQuestionDao;

    /**
     * 同步问卷调查问卷模板到ES
     */
    @Test
    public void indexTemplateToES() {
        final List<Template> all = templateService.getAll();
        if(all == null)
            return;
        final ArrayList<ESTemplate> esTemplates = new ArrayList<>();
        all.forEach(t ->{
            final long id = t.getId();
            final ESTemplate esTemplate = new ESTemplate();
            BeanUtils.copyProperties(t,esTemplate);
            final List<TemplateQuestion> questionList = templateQuestionDao.getByTemplateId(id);
            if(questionList == null)
                return;
            final ArrayList<ESTemplateQuestion> qList = new ArrayList<>();
            questionList.forEach(q ->{
                final ESTemplateQuestion esTemplateQuestion = new ESTemplateQuestion();
                BeanUtils.copyProperties(q,esTemplateQuestion);
                qList.add(esTemplateQuestion);
            });
            esTemplate.setQuestions(qList);
            esTemplates.add(esTemplate);
        });
        System.out.println("===============开始同步数据到ES.....");
        templateRepository.saveAll(esTemplates);
        System.out.printf("===============同步数据到ES完成，共同步%s条数据%n", esTemplates.size());
    }

    @Test
    public void testAdd() {
        final ESTemplate esTemplate = new ESTemplate();
        esTemplate.setId(1);
        esTemplate.setName("大学生");
        esTemplate.setCreateDate(new Date());
        esTemplate.setQuestionCount(12);
        final ArrayList<ESTemplateQuestion> list = new ArrayList<>();
        final ESTemplateQuestion q = new ESTemplateQuestion();
        q.setId(1L);
        q.setQuestionNum(1);
        q.setQuestionOrder(1);
        q.setQuestionTitle("你的性别？");
        list.add(q);
        esTemplate.setQuestions(list);
        templateRepository.save(esTemplate);
    }
    @Test
    public void testQuery() {
        final SearchHits<ESTemplate> searchHits = templateRepository.find("大学生");
        searchHits.forEach(System.out::println);
    }

}
