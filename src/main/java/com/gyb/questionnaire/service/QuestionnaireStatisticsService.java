package com.gyb.questionnaire.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gyb.questionnaire.dao.PaperAnswerDao;
import com.gyb.questionnaire.dao.PaperDao;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionOptionDao;
import com.gyb.questionnaire.dto.QuestionnaireQuestionStatisticDTO;
import com.gyb.questionnaire.dto.QuestionnaireStatisticDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;
import com.gyb.questionnaire.entity.User;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 问卷结果分析与统计Service
 *
 * @author geng
 * 2020/11/27
 */
@Service
public class QuestionnaireStatisticsService {
    private final PaperDao paperDao;
    private final PaperAnswerDao paperAnswerDao;
    private final QuestionnaireDao questionnaireDao;
    private final QuestionnaireQuestionDao questionnaireQuestionDao;
    private final QuestionnaireQuestionOptionDao questionnaireQuestionOptionDao;

    public QuestionnaireStatisticsService(PaperDao paperDao, PaperAnswerDao paperAnswerDao,
                        QuestionnaireDao questionnaireDao,
                        QuestionnaireQuestionDao questionnaireQuestionDao,
                        QuestionnaireQuestionOptionDao questionnaireQuestionOptionDao) {
        this.paperDao = paperDao;
        this.paperAnswerDao = paperAnswerDao;
        this.questionnaireDao = questionnaireDao;
        this.questionnaireQuestionDao = questionnaireQuestionDao;
        this.questionnaireQuestionOptionDao = questionnaireQuestionOptionDao;
    }


    public QuestionnaireStatisticDTO statistic(long questionnaireId) {
        final User loginUser = LoginUserService.getLoginUser();
        if (loginUser == null)
            return null;
        final Questionnaire questionnaire = questionnaireDao.findByIdAndUserId(questionnaireId, loginUser.getId());
        if (questionnaire == null)
            return null;
        QuestionnaireStatisticDTO dto = new QuestionnaireStatisticDTO();
        dto.setQuestionnaire(questionnaire); //将问卷放到DTO中
        List<QuestionnaireQuestion> questions = questionnaireQuestionDao.findByQuestionnaireId(questionnaireId);
        if(questions == null || questions.isEmpty())
            return dto;
        questions.sort(Comparator.comparingInt(QuestionnaireQuestion::getQuestionOrder));
        List<Long> paperIds = paperDao.findIdsByQuestionnaireId(questionnaireId);
        questionnaire.setPaperCount(paperIds==null?0:paperIds.size());
        final List<QuestionnaireQuestionStatisticDTO> questionDTOs = new ArrayList<>();
        for (QuestionnaireQuestion question : questions) {
            final int questionType = question.getQuestionType();
            if(questionType == 5) //问题类型是5对应分割线,直接跳过下面的步骤
                continue;
            final QuestionnaireQuestionStatisticDTO questionDTO = new QuestionnaireQuestionStatisticDTO();
            BeanUtils.copyProperties(question,questionDTO);
            if(questionType == 3 || questionType == 4) { //对于单选和多选统计选项被选次数
                List<QuestionnaireQuestionOption> options = questionnaireQuestionOptionDao.findByQuestionId(question.getId());
                if(options != null && paperIds != null && !paperIds.isEmpty()) {
                    Map<Long, Integer> optionCountMap = countCheckedOption(paperIds, question.getId());
                    for (QuestionnaireQuestionOption option : options) {
                        final Integer count = optionCountMap.get(option.getId());
                        option.setCheckedCount(count==null?0:count);
                    }
                    options.sort(Comparator.comparingInt(QuestionnaireQuestionOption::getOptionOrder));
                }
                questionDTO.setOptions(options);
            }else if(questionType == 1 || questionType == 2){ //对于填空题选出5条数据
                final List<String> answers = paperAnswerDao.findAnswerByQuestionId(question.getId(), 5);
                questionDTO.setAnswers(answers);
            }
            questionDTOs.add(questionDTO);
        }
        dto.setQuestions(questionDTOs);
        return dto;
    }

    private Map<Long, Integer> countCheckedOption(List<Long> paperIds, Long questionId) {
        if(paperIds == null || paperIds.isEmpty())
            return null;
        final HashMap<Long, Integer> map = new HashMap<>();
        for (Long paperId : paperIds) {
            final String answer = paperAnswerDao.findAnswerByPaperIdAndQuestionId(paperId, questionId);
            if(answer != null && answer.length() >0){
                final String[] split = answer.split(",");
                if(split.length >0){
                    for (String s : split) {
                        if(s == null || s.length() == 0)
                            continue;
                        final Integer currValue = map.get(Long.valueOf(s));
                        map.put(Long.valueOf(s),currValue == null?1:currValue+1);
                    }
                }
            }
        }
        return map;
    }
}
