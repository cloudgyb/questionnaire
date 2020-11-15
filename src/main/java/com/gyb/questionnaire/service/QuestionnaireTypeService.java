package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.QuestionnaireTemplateTypeDao;
import com.gyb.questionnaire.entity.QuestionnaireType;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author geng
 * 2020/11/8
 */

@Service
public class QuestionnaireTypeService {
    private final QuestionnaireTemplateTypeDao templateTypeDao;

    public QuestionnaireTypeService(QuestionnaireTemplateTypeDao templateTypeDao) {
        this.templateTypeDao = templateTypeDao;
    }

    public List<QuestionnaireType> getAll() {
        return templateTypeDao.findAll();
    }

    public QuestionnaireType getById(long id) {
        return templateTypeDao.find(id);
    }

}
