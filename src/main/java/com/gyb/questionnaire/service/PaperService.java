package com.gyb.questionnaire.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.controller.form.PaperForm;
import com.gyb.questionnaire.controller.form.QuestionAnswer;
import com.gyb.questionnaire.dao.PaperAnswerDao;
import com.gyb.questionnaire.dao.PaperDao;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.dto.AddressInfo;
import com.gyb.questionnaire.entity.Paper;
import com.gyb.questionnaire.entity.PaperAnswer;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.Client;
import com.gyb.questionnaire.util.ClientUtil;
import com.gyb.questionnaire.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 答卷Service
 *
 * @author geng
 * 2020/11/23
 */
@Slf4j
@Service
public class PaperService {
    private final PaperDao paperDao;
    private final PaperAnswerDao paperAnswerDao;
    private final QuestionnaireDao questionnaireDao;
    @Resource
    private RestTemplate restTemplate;
    private ThreadPoolExecutor threadPool;

    public PaperService(PaperDao paperDao, PaperAnswerDao paperAnswerDao,
                        QuestionnaireDao questionnaireDao) {
        this.paperDao = paperDao;
        this.paperAnswerDao = paperAnswerDao;
        this.questionnaireDao = questionnaireDao;
    }

    @PostConstruct
    public void init(){
        if(threadPool == null){
            threadPool = new ThreadPoolExecutor(3,5,5*60, TimeUnit.SECONDS,
                    new LinkedBlockingDeque<>(1000));
        }
    }

    @PreDestroy
    public void destroy(){
        if(threadPool != null)
            threadPool.shutdown();
    }

    @Transactional
    public ResponseResult submitPaper(PaperForm paperForm) {
        final String questionnaireId = paperForm.getQuestionnaireId();
        final Questionnaire questionnaire = questionnaireDao.find(questionnaireId);
        //如果问卷不存在或者问卷不在“发布”状态，对于参与者来说并不关心这些，直接返回OK
        if (questionnaire == null || questionnaire.getStatus() != 1)
            return ResponseResult.ok();
        final Client clientInfo = ClientUtil.getClientInfo();
        Paper paper = paperDao.findByQuestionnaireIdAndIp(questionnaireId, clientInfo.getIp());
        if (paper != null) {
            return updatePaper(paper, paperForm);
        }
        paper = new Paper();
        String paperId = RandomUtil.uuid();
        paper.setId(paperId);
        paper.setQuestionnaireId(questionnaireId);
        paper.setSubmitTime(new Date());
        paper.setElapsedTime(paperForm.getElapsedTime() / 1000); //form传的是毫秒，/1000转为秒
        paper.setIp(clientInfo.getIp());
        paper.setAddress("");
        paper.setSource(paperForm.getSource());
        paperDao.insert(paper);
        saveAnswer(paperForm.getResultList(), paperId);
        Paper finalPaper = paper;
        threadPool.execute(() -> updatePaperAddress(clientInfo.getIp(), finalPaper));
        return ResponseResult.ok();
    }

    private void updatePaperAddress(String ip, Paper paper) {
        if (ip == null || "".equals(ip) || "127.0.0.1".equals(ip))
            return;
        String url = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip="+ip;
        try {
            final String resp = restTemplate.getForObject(url, String.class);
            final ObjectMapper objectMapper = new ObjectMapper();
            AddressInfo address = objectMapper.readValue(resp,AddressInfo.class);
            String addressStr = "";
            if (address != null) {
                if (StringUtils.hasText(address.getPro())) {
                    addressStr += address.getPro();
                    addressStr += " ";
                }
                if (StringUtils.hasText(address.getCity())) {
                    addressStr += address.getCity();
                    addressStr += " ";
                }
                if (StringUtils.hasText(address.getRegion())) {
                    addressStr += address.getRegion();
                }
            }
            addressStr = addressStr.trim();
            if (StringUtils.hasText(addressStr)) {
                paper.setAddress(addressStr);
                paperDao.update(paper);
            }
        } catch (Exception e) {
            log.error("更新答卷地址失败！",e);
        }
    }

    private void saveAnswer(List<QuestionAnswer> resultList, String paperId) {
        if (resultList == null)
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

    private ResponseResult updatePaper(Paper p, PaperForm form) {
        return ResponseResult.ok();
    }

    public int paperCount(String questionnaireId) {
        return paperDao.countByQuestionnaireId(questionnaireId);
    }

    /**
     * 获取问卷下的答卷
     *
     * @param questionnaireId 问卷id
     */
    public List<Paper> getByQuestionnaireId(String questionnaireId) {
        final User loginUser = LoginUserService.getLoginUser();
        if (loginUser == null)
            return null;
        final Questionnaire questionnaire = questionnaireDao.findByIdAndUserId(questionnaireId, loginUser.getId());
        if (questionnaire == null)
            return null;
        return paperDao.findByQuestionnaireId(questionnaireId);
    }
}
