package com.gyb.questionnaire.service;

import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.controller.form.PaperForm;
import com.gyb.questionnaire.controller.form.QuestionAnswer;
import com.gyb.questionnaire.dao.PaperAnswerDao;
import com.gyb.questionnaire.dao.PaperDao;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.entity.Paper;
import com.gyb.questionnaire.entity.PaperAnswer;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.util.Client;
import com.gyb.questionnaire.util.ClientUtil;
import com.gyb.questionnaire.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * 答卷Service
 *
 * @author geng
 * 2020/11/23
 */
@Service
public class PaperService {
    private final PaperDao paperDao;
    private final PaperAnswerDao paperAnswerDao;
    private final QuestionnaireDao questionnaireDao;

    public PaperService(PaperDao paperDao, PaperAnswerDao paperAnswerDao,
                        QuestionnaireDao questionnaireDao) {
        this.paperDao = paperDao;
        this.paperAnswerDao = paperAnswerDao;
        this.questionnaireDao = questionnaireDao;
    }

    @Transactional
    public ResponseResult submitPaper(PaperForm paperForm) {
        final String questionnaireId = paperForm.getQuestionnaireId();
        final Questionnaire questionnaire = questionnaireDao.find(questionnaireId);
        //如果问卷不存在或者问卷不在“发布”状态，对于参与者来说并不关心这些，直接返回OK
        if(questionnaire == null || questionnaire.getStatus()!=1)
            return ResponseResult.ok();
        final Client clientInfo = ClientUtil.getClientInfo();
        Paper paper = paperDao.findByQuestionnaireIdAndIp(questionnaireId, clientInfo.getIp());
        if(paper != null){
            return updatePaper(paper,paperForm);
        }
        paper = new Paper();
        String paperId = RandomUtil.uuid();
        paper.setId(paperId);
        paper.setQuestionnaireId(questionnaireId);
        paper.setSubmitTime(new Date(System.currentTimeMillis()));
        paper.setElapsedTime(paperForm.getElapsedTime());
        paper.setIp(clientInfo.getIp());
        paper.setAddress("");
        paper.setSource(paperForm.getSource());
        paperDao.insert(paper);
        saveAnswer(paperForm.getResultList(),paperId);
        return ResponseResult.ok();
    }

    private void saveAnswer(List<QuestionAnswer> resultList,String paperId) {
        if(resultList == null)
            return;
        for (QuestionAnswer questionAnswer : resultList) {
            final PaperAnswer answer = new PaperAnswer();
            answer.setId(RandomUtil.uuid());
            answer.setPaperId(paperId);
            answer.setQuestionId(questionAnswer.getQuestionId());
            answer.setAnswer(questionAnswer.getResult());
            paperAnswerDao.insert(answer);
        }
    }

    private ResponseResult updatePaper(Paper p,PaperForm form){
        return ResponseResult.ok();
    }

    public int paperCount(String questionnaireId){
        return paperDao.countByQuestionnaireId(questionnaireId);
    }
}
