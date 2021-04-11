package com.gyb.questionnaire.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.gyb.questionnaire.config.QuestionnaireStatusEnum;
import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.dao.PaperAnswerDao;
import com.gyb.questionnaire.dao.PaperDao;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionOptionDao;
import com.gyb.questionnaire.dto.QuestionnaireDTO;
import com.gyb.questionnaire.dto.QuestionnaireQuestionDTO;
import com.gyb.questionnaire.dto.QuestionnaireTemplateDTO;
import com.gyb.questionnaire.dto.TemplateQuestionDTO;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;
import com.gyb.questionnaire.entity.TemplateQuestionOption;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.HttpServletUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;

/**
 * 调查问卷Service
 *
 * @author geng
 * 2020/11/8
 */

@Service
public class QuestionnaireService extends QuestionnaireBaseService {
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
        super(questionnaireDao);
        this.questionnaireDao = questionnaireDao;
        this.questionDao = questionDao;
        this.optionDao = optionDao;
        this.templateService = templateService;
        this.paperDao = paperDao;
        this.paperAnswerDao = paperAnswerDao;
    }

    /**
     * 获取该登陆用户下的问卷列表
     */
    public Page<Questionnaire> getUserQuestionnaireList(int page,int size) {
        final User u = LoginUserService.getLoginUser();
        final Page<Questionnaire> pages = PageHelper.startPage(page, size);
        final List<Questionnaire> questionnaireList = questionnaireDao.findByUserId(u.getId());
        if(questionnaireList != null){
            for (Questionnaire questionnaire : questionnaireList) {
                if(questionnaire.getStatus() > 0){ //对于已经发布的问卷统计答卷数量
                   int paperCount = paperDao.countByQuestionnaireId(questionnaire.getId());
                   questionnaire.setPaperCount(paperCount);
                }
            }
        }
        return pages;
    }

    @Transactional
    public long add(String name, String greeting) {
        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(name);
        questionnaire.setGreeting(greeting);
        questionnaire.setCreateDate(new Date());
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        questionnaire.setUserId(u.getId());
        questionnaire.setQuestionCount(0);
        questionnaire.setInvokeCount(0);
        questionnaire.setStatus(0);
        questionnaireDao.add(questionnaire);
        return questionnaire.getId();
    }

    /**
     * 获取问卷调查模板详情
     * @param questionnaireId 问卷ID
     * @see QuestionnaireDTO
     */
    public QuestionnaireDTO getUserQuestionnaireDetail(long questionnaireId) {
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

    private List<QuestionnaireQuestionDTO> getQuestionnaireQuestionsDTO(long questionnaireId) {
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

    @Transactional
    public ResponseResult saveQuestionnaire(QuestionnaireDTO dto) {
        ResponseResult errR = ResponseResult.error("问卷不存在或已被删除",null);
        if(null == dto.getId()){
            return errR;
        }
        final User u = LoginUserService.getLoginUser();
        final Questionnaire questionnaire = questionnaireDao.findByIdAndUserId(dto.getId(),u.getId());
        if(questionnaire == null)
            return errR;
        if(questionnaire.getStatus() != 0){
            String errMsg = questionnaire.getStatus()==1?"问卷已发布无法保存更新！":"问卷已结束无法编辑！";
            return ResponseResult.error(errMsg,null);
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
            if(null != question.getId()){
                questionDao.update(question);
            }else{
                questionDao.add(question);
            }
            //3、对单选和多选题更新选项
            if(question.getQuestionType() == 3 || question.getQuestionType() == 4)
                updateQuestionOptions(question.getOptions(),question.getId());
        }
        return ResponseResult.ok();
    }

    private void updateQuestionOptions(List<QuestionnaireQuestionOption> options, long questionId) {
        if(options == null)
            return;
        for (QuestionnaireQuestionOption option : options) {
            option.setQuestionId(questionId);
            if(option.getId() != null){
                optionDao.update(option);
            }else {
                optionDao.add(option);
            }
        }
    }

    /**
     * 删除问卷中问题
     */
    @Transactional
    public void deleteQuestion(long questionId) {
        questionDao.delete(questionId);
        optionDao.deleteByQuestionId(questionId);
    }
    /**
     * 删除问卷中问题的选项
     */
    @Transactional
    public void deleteQuestionOption(long optionId) {
        optionDao.delete(optionId);
    }

    @Transactional
    public ResponseResult publishQuestionnaire(long questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除",null);
        questionnaire.setStatus(QuestionnaireStatusEnum.PUBLISHED.intValue());
        questionnaireDao.update(questionnaire);
        return ResponseResult.ok();
    }

    @Transactional
    public ResponseResult stopQuestionnaire(long questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除",null);
        questionnaire.setStatus(QuestionnaireStatusEnum.FINISHED.intValue());
        questionnaireDao.update(questionnaire);
        return ResponseResult.ok();
    }

    /**
     * 删除问卷
     * @param questionnaireId 问卷id
     */
    @Transactional
    public ResponseResult delete(long questionnaireId) {
        final Questionnaire questionnaire = getUserQuestionnaire(questionnaireId);
        if(questionnaire == null)
            return ResponseResult.error("问卷不存在或已被删除",null);

        List<Long> questionIds = questionDao.findIdByQuestionnaireId(questionnaireId);
        if(questionIds != null){
            for (Long questionId : questionIds) { //将每一个问题下的选项删除
                optionDao.deleteByQuestionId(questionId);
            }
        }
        //删除问题
        questionDao.deleteByQuestionnaireId(questionnaireId);
        //删除问卷
        questionnaireDao.delete(questionnaireId);
        final List<Long> paperIds = paperDao.findIdsByQuestionnaireId(questionnaireId);
        if(paperIds != null){
            for (Long paperId : paperIds) {
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
    @Transactional
    public Long addByTemplate(long templateId) {
        final QuestionnaireTemplateDTO template = templateService.getTemplateDetail(templateId);
        if(template == null)
            return null;
        Long id = copyQuestionnaire(template);
        copyQuestion(template.getQuestions(),id);
        return id;
    }

    private void copyQuestion(List<TemplateQuestionDTO> questions, Long questionnaireId) {
        if(questions == null)
            return;
        for (TemplateQuestionDTO question : questions) {
            final QuestionnaireQuestion q = new QuestionnaireQuestion();
            int qType = question.getQuestionType();
            q.setQuestionnaireId(questionnaireId);
            q.setQuestionTitle(question.getQuestionTitle());
            q.setQuestionType(qType);
            q.setQuestionNum(question.getQuestionNum());
            q.setQuestionOrder(question.getQuestionOrder());
            q.setInputPlaceholder(question.getInputPlaceholder());
            questionDao.add(q);
            if(qType == 3 || qType == 4) //只有单选题和多选题才有选项，才需要复制选项
                copyQuestionOption(question.getQuestionOptions(),q.getId());
        }
    }

    private void copyQuestionOption(List<TemplateQuestionOption> questionOptions, long questionId) {
        if(questionOptions == null)
            return;
        for (TemplateQuestionOption questionOption : questionOptions) {
            final QuestionnaireQuestionOption option = new QuestionnaireQuestionOption();
            option.setQuestionId(questionId);
            option.setOptionText(questionOption.getOptionText());
            option.setOptionOrder(questionOption.getOptionOrder());
            optionDao.add(option);
        }
    }

    private Long copyQuestionnaire(QuestionnaireTemplateDTO template) {
        final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
        if (u == null)
            return null;
        final Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(template.getName());
        questionnaire.setGreeting("感谢参与，请大家根据自身情况如实填写，谢谢大家配合!");
        questionnaire.setQuestionCount(template.getQuestionCount());
        questionnaire.setStatus(0);
        questionnaire.setUserId(u.getId());
        questionnaire.setCreateDate(new Date());
        questionnaire.setTemplateId(template.getId());
        questionnaire.setTypeId(template.getTypeId());
        questionnaireDao.add(questionnaire);
        return questionnaire.getId();
    }
}
