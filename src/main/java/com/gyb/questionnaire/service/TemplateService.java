package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.TemplateDao;
import com.gyb.questionnaire.dto.TemplateQuestionDTO;
import com.gyb.questionnaire.dto.QuestionnaireTemplateDTO;
import com.gyb.questionnaire.entity.Template;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 调查问卷模板Service
 *
 * @author geng
 * 2020/11/8
 */

@Service
@Transactional
public class TemplateService {
    private final TemplateDao templateDao;
    private final TemplateQuestionService questionService;

    public TemplateService(TemplateDao templateDao,
                           TemplateQuestionService questionService) {
        this.templateDao = templateDao;
        this.questionService = questionService;
    }

    public List<Template> getAll() {
        return templateDao.findAll();
    }

    public List<Template> getTypeId(long typeId) {
        return templateDao.findByTypeId(typeId);
    }
    /**
     * 封装成“调查问卷模板”DTO
     * @param templateId 模板
     * @return QuestionnaireTemplateDTO
     */
    public QuestionnaireTemplateDTO getTemplateDetail(long templateId){
        Template qt = templateDao.find(templateId);
        QuestionnaireTemplateDTO dto = null;
        if(qt != null) {
            dto = new QuestionnaireTemplateDTO();
            dto.setId(templateId);
            dto.setName(qt.getName());
            dto.setAuthorId(qt.getAuthorId());
            dto.setCreateDate(qt.getCreateDate());
            dto.setPublishDate(qt.getPublishDate());
            dto.setQuestionCount(qt.getQuestionCount());
            dto.setTypeId(qt.getTypeId());
            List<TemplateQuestionDTO> questionDTO = questionService.getDetailByTemplate(templateId);
            dto.setQuestions(questionDTO);
        }
        return dto;
    }
}
