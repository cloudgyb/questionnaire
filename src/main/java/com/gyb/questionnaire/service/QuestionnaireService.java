package com.gyb.questionnaire.service;

import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.dao.*;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.dto.QuestionnaireQuestionDTO;
import com.gyb.questionnaire.dto.QuestionnaireTemplateDTO;
import com.gyb.questionnaire.dto.TemplateQuestionDTO;
import com.gyb.questionnaire.entity.*;
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
    private final TemplateService templateService;
    private final PaperDao paperDao;
    private final PaperAnswerDao paperAnswerDao;

    public QuestionnaireService(QuestionnaireDao questionnaireDao,
                                QuestionnaireQuestionDao questionDao,
                                QuestionnaireQuestionOptionDao optionDao,
                                TemplateService templateService,
                                PaperDao paperDao,
                                PaperAnswerDao paperAnswerDao) {
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
        this.templateService = templateService;
        this.paperDao = paperDao;
        this.paperAnswerDao = paperAnswerDao;
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
        final List<Questionnaire> questionnaireList = questionnaireDao.findByUserId(u.getId());
        if(questionnaireList != null){
            for (Questionnaire questionnaire : questionnaireList) {
                if(questionnaire.getStatus() > 0){ //对于已经发布的问卷统计答卷数量
                   int paperCount = paperDao.countByQuestionnaireId(questionnaire.getId());
                   questionnaire.setPaperCount(paperCount);
                }
            }
        }
        return questionnaireList;
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
        return getQuestionnaireDetail(questionnaire);
    }

    public QuestionnaireDTO getQuestionnaireDetail(Questionnaire questionnaire){
        final QuestionnaireDTO dto = new QuestionnaireDTO();
        dto.setId(questionnaire.getId());
        dto.setCreateDate(questionnaire.getCreateDate());
        dto.setGreeting(questionnaire.getGreeting());
        dto.setName(questionnaire.getName());
        dto.setQuestionCount(questionnaire.getQuestionCount());
        List<QuestionnaireQuestionDTO> questionsDTO = getQuestionnaireQuestionsDTO(questionnaire.getId());
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

    public ResponseResult stopQuestionnaire(String questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除");
        questionnaire.setStatus(2);
        questionnaireDao.update(questionnaire);
        return ResponseResult.ok();
    }

    /**
     * 删除问卷
     * @param questionnaireId 问卷id
     */
    public ResponseResult delete(String questionnaireId) {
        if(!StringUtils.hasText(questionnaireId))
            return ResponseResult.error("问卷不存在或已被删除");
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除");

        List<String> questionIds = questionDao.findIdByQuestionnaireId(questionnaireId);
        if(questionIds != null){
            for (String questionId : questionIds) { //将每一个问题下的选项删除
                optionDao.deleteByQuestionId(questionId);
            }
        }
        //删除问题
        questionDao.deleteByQuestionnaireId(questionnaireId);
        //删除问卷
        questionnaireDao.delete(questionnaireId);
        final List<String> paperIds = paperDao.findIdsByQuestionnaireId(questionnaireId);
        if(paperIds != null){
            for (String paperId : paperIds) {
                //删除答卷答案
                paperAnswerDao.deleteByPaperId(paperId);
            }
        }
        //删除答卷
        paperDao.deleteByQuestionnaireId(questionnaireId);
        return ResponseResult.ok();
    }

    /**
     * 以模板创建调查问卷
     * @param templateId 模板id
     * @return 如果创建成功返回问卷id
     */
    public String addByTemplate(long templateId) {
        final QuestionnaireTemplateDTO template = templateService.getTemplateDetail(templateId);
        if(template == null)
            return null;
        String id = copyQuestionnaire(template);
        copyQuestion(template.getQuestions(),id);
        return id;
    }

    private void copyQuestion(List<TemplateQuestionDTO> questions, String questionnaireId) {
        if(questions == null)
            return;
        for (TemplateQuestionDTO question : questions) {
            final QuestionnaireQuestion q = new QuestionnaireQuestion();
            final String qId = RandomUtil.uuid();
            int qType = question.getQuestionType();
            q.setId(qId);
            q.setQuestionnaireId(questionnaireId);
            q.setQuestionTitle(question.getQuestionTitle());
            q.setQuestionType(qType);
            q.setQuestionNum(question.getQuestionNum());
            q.setQuestionOrder(question.getQuestionOrder());
            q.setInputPlaceholder(question.getInputPlaceholder());
            questionDao.add(q);
            if(qType == 3 || qType == 4) //只有单选题和多选题才有选项，才需要复制选项
                copyQuestionOption(question.getQuestionOptions(),qId);
        }
    }

    private void copyQuestionOption(List<TemplateQuestionOption> questionOptions, String questionId) {
        if(questionOptions == null)
            return;
        for (TemplateQuestionOption questionOption : questionOptions) {
            final QuestionnaireQuestionOption option = new QuestionnaireQuestionOption();
            final String uuid = RandomUtil.uuid();
            option.setId(uuid);
            option.setQuestionId(questionId);
            option.setOptionText(questionOption.getOptionText());
            option.setOptionOrder(questionOption.getOptionOrder());
            optionDao.add(option);
        }
    }

    private String copyQuestionnaire(QuestionnaireTemplateDTO template) {
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return null;
        final Questionnaire questionnaire = new Questionnaire();
        final String uuid = RandomUtil.uuid();
        questionnaire.setId(uuid);
        questionnaire.setName(template.getName());
        questionnaire.setGreeting("感谢参与，请大家根据自身情况如实填写，谢谢大家配合!");
        questionnaire.setQuestionCount(template.getQuestionCount());
        questionnaire.setStatus(0);
        questionnaire.setUserId(u.getId());
        questionnaire.setCreateDate(new Date());
        questionnaire.setTemplateId(template.getId());
        questionnaire.setTypeId(template.getTypeId());
        questionnaireDao.add(questionnaire);
        return uuid;
    }
}
