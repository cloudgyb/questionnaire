package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.TemplateQuestionOptionDao;
import com.gyb.questionnaire.entity.TemplateQuestionOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class TemplateQuestionOptionService {
    private final TemplateQuestionOptionDao questionOptionDao;

    @Autowired
    public TemplateQuestionOptionService(TemplateQuestionOptionDao questionOptionDao) {
        this.questionOptionDao = questionOptionDao;
    }

    /**
     * 添加问题选项
     * @param questionOption 问题选项实体
     * @return 新添加的问题id
     */
    public int addOption(TemplateQuestionOption questionOption){
        return questionOptionDao.add(questionOption);
    }

    public void updateOption(TemplateQuestionOption qo){
        int n = questionOptionDao.update(qo);
        if( n < 1)
            log.warn("更新问题选项失败！id="+qo.getId());
    }

    /**
     * 获取“问题”下所有的选项
     * @param questionId 问题id
     * @return 问题选项List
     */
    public List<TemplateQuestionOption> getByQuestionId(int questionId){
        return questionOptionDao.getByQuestionId(questionId);
    }

    /**
     * 删除“问题”下所有的选项
     * @param questionId 问题id
     */
    public void deleteByQuestionId(long questionId){
        questionOptionDao.deleteByQuestionId(questionId);
    }

    /**
     * 通过id删除“选项”
     * @param questionOptionId “问题选项id”
     */
    public void deleteById(long questionOptionId) {
        questionOptionDao.delete(questionOptionId);
    }


}
