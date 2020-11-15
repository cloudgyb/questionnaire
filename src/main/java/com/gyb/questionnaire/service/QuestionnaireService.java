package com.gyb.questionnaire.service;

import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionOptionDao;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.dto.QuestionnaireQuestionDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.HttpServletUtil;
import com.gyb.questionnaire.util.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;

/**
 * 调查问卷Service
 *
 * @author geng
 * 2020/11/8
 */

@Service
@Transactional
public class QuestionnaireService {
    private final QuestionnaireDao questionnaireDao;
    private final QuestionnaireQuestionDao questionDao;
    private final QuestionnaireQuestionOptionDao optionDao;

    public QuestionnaireService(QuestionnaireDao questionnaireDao,
                                QuestionnaireQuestionDao questionDao,
                                QuestionnaireQuestionOptionDao optionDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
    }

    /**
     * 根据id获取问卷
     *
     * @param questionnaireId 问卷id
     */
    public Questionnaire get(String questionnaireId) {
        return questionnaireDao.find(questionnaireId);
    }

    /**
     * 根据问卷id获取登录用户的问卷，如果该登陆用户下没有对应的问卷返回null
     *
     * @param questionnaireId 问卷id
     */
    public Questionnaire getUserQuestionnaire(String questionnaireId) {
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return null;
        return questionnaireDao.findByIdAndUserId(questionnaireId, u.getId());
    }

    /**
     * 获取该登陆用户下的问卷列表
     */
    public List<Questionnaire> getUserQuestionnaireList() {
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return null;
        return questionnaireDao.findByUserId(u.getId());
    }

    public String add(String name, String greeting) {
        final Questionnaire questionnaire = new Questionnaire();
        final String uuid = RandomUtil.uuid();
        questionnaire.setId(uuid);
        questionnaire.setName(name);
        questionnaire.setGreeting(greeting);
        questionnaire.setCreateDate(new Date());
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return "";
        questionnaire.setUserId(u.getId());
        questionnaire.setQuestionCount(0);
        questionnaire.setInvokeCount(0);
        questionnaire.setStatus(0);
        questionnaireDao.add(questionnaire);
        return uuid;
    }

    /**
     * 获取问卷调查模板详情
     * @param questionnaireId 问卷ID
     * @see QuestionnaireDTO
     */
    public QuestionnaireDTO getUserQuestionnaireDetail(String questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if (questionnaire == null)
            return null;
        final QuestionnaireDTO dto = new QuestionnaireDTO();
        dto.setId(questionnaireId);
        dto.setCreateDate(questionnaire.getCreateDate());
        dto.setGreeting(questionnaire.getGreeting());
        dto.setName(questionnaire.getName());
        dto.setQuestionCount(questionnaire.getQuestionCount());
        List<QuestionnaireQuestionDTO> questionsDTO = getQuestionnaireQuestionsDTO(questionnaireId);
        questionsDTO.sort(Comparator.comparing(QuestionnaireQuestionDTO::getQuestionOrder));
        dto.setQuestions(questionsDTO);
        return dto;
    }

    private List<QuestionnaireQuestionDTO> getQuestionnaireQuestionsDTO(String questionnaireId) {
        List<QuestionnaireQuestionDTO> dtos = new ArrayList<>();
        List<QuestionnaireQuestion> questions = questionDao.findByQuestionnaireId(questionnaireId);
        for (QuestionnaireQuestion q : questions) {
            final QuestionnaireQuestionDTO dto = new QuestionnaireQuestionDTO();
            dto.setId(q.getId());
            dto.setQuestionnaireId(questionnaireId);
            dto.setQuestionTitle(q.getQuestionTitle());
            dto.setQuestionNum(q.getQuestionNum());
            dto.setQuestionOrder(q.getQuestionOrder());
            dto.setQuestionType(q.getQuestionType());
            dto.setInputPlaceholder(q.getInputPlaceholder());
            List<QuestionnaireQuestionOption> options = optionDao.findByQuestionId(q.getId());
            options.sort(Comparator.comparing(QuestionnaireQuestionOption::getOptionOrder));
            dto.setOptions(options);
            dtos.add(dto);
        }
        return dtos;
    }

    public ResponseResult saveQuestionnaire(QuestionnaireDTO dto) {
        ResponseResult errR = ResponseResult.error("问卷不存在或已被删除");
        if(!StringUtils.hasText(dto.getId())){
            return errR;
        }
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return ResponseResult.error("用户登录过期！");
        final Questionnaire questionnaire = questionnaireDao.findByIdAndUserId(dto.getId(),u.getId());
        if(questionnaire == null)
            return errR;
        if(questionnaire.getStatus() != 0){
            String errMsg = questionnaire.getStatus()==1?"问卷已发布无法保存更新！":"问卷已结束无法编辑！";
            return ResponseResult.error(errMsg);
        }
        questionnaire.setName(dto.getName());
        questionnaire.setGreeting(dto.getGreeting());
        final List<QuestionnaireQuestionDTO> questions = dto.getQuestions();
        questionnaire.setQuestionCount(questions !=null?questions.size():0);
        //1、先更新问卷信息
        questionnaireDao.update(questionnaire);
        //2、更新问题
        if(questions == null)
            return ResponseResult.ok();
        for (QuestionnaireQuestionDTO question : questions) {
            question.setQuestionnaireId(questionnaire.getId());
            if(StringUtils.hasText(question.getId())){
                questionDao.update(question);
            }else{
                question.setId(RandomUtil.uuid());
                questionDao.add(question);
            }
            //3、对单选和多选题更新选项
            if(question.getQuestionType() == 3 || question.getQuestionType() == 4)
                updateQuestionOptions(question.getOptions(),question.getId());
        }
        return ResponseResult.ok();
    }

    private void updateQuestionOptions(List<QuestionnaireQuestionOption> options, String questionId) {
        if(options == null)
            return;
        for (QuestionnaireQuestionOption option : options) {
            option.setQuestionId(questionId);
            if(StringUtils.hasText(option.getId())){
                optionDao.update(option);
            }else {
                option.setId(RandomUtil.uuid());
                optionDao.add(option);
            }
        }
    }

    /**
     * 删除问卷中问题
     */
    public void deleteQuestion(String questionId) {
        questionDao.delete(questionId);
        optionDao.deleteByQuestionId(questionId);
    }
    /**
     * 删除问卷中问题的选项
     */
    public void deleteQuestionOption(String optionId) {
        optionDao.delete(optionId);
    }

    public ResponseResult publishQuestionnaire(String questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除");
        questionnaire.setStatus(1);
        questionnaireDao.update(questionnaire);
        return ResponseResult.ok();
    }
}
