package com.gyb.questionnaire.service;

import java.util.Date;
import java.util.List;

import com.gyb.questionnaire.config.QuestionnaireStatusEnum;
import com.gyb.questionnaire.controller.ResponseResult;
import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionDao;
import com.gyb.questionnaire.dao.QuestionnaireQuestionOptionDao;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.QuestionnaireQuestion;
import com.gyb.questionnaire.entity.QuestionnaireQuestionOption;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author cloudgyb
 * 2021/4/7 10:28
 */
@Service
public class QuestionnaireCloneService extends QuestionnaireBaseService {
	private final QuestionnaireDao questionnaireDao;
	private final QuestionnaireQuestionDao questionnaireQuestionDao;
	private final QuestionnaireQuestionOptionDao questionnaireQuestionOptionDao;

	public QuestionnaireCloneService(QuestionnaireDao questionnaireDao, QuestionnaireQuestionDao questionnaireQuestionDao, QuestionnaireQuestionOptionDao questionnaireQuestionOptionDao) {
		super(questionnaireDao);
		this.questionnaireDao = questionnaireDao;
		this.questionnaireQuestionDao = questionnaireQuestionDao;
		this.questionnaireQuestionOptionDao = questionnaireQuestionOptionDao;
	}

	/**
	 * 复制调查问卷
	 * @param sourceQuestionnaireId 调查问卷id
	 * @param newName 新问卷的名字
	 * @param newDesc 新问卷描述
	 */
	@Transactional
	public ResponseResult copy(long sourceQuestionnaireId, String newName, String newDesc) {
		final Questionnaire questionnaire = getUserQuestionnaire(sourceQuestionnaireId);
		if (questionnaire == null)
			return ResponseResult.error("问卷不存在或已被删除", null);
		List<QuestionnaireQuestion> questions = questionnaireQuestionDao.findByQuestionnaireId(sourceQuestionnaireId);
		List<Long> sourceQuestionIds = questionnaireQuestionDao.findIdByQuestionnaireId(sourceQuestionnaireId);
		//创建新的调查问卷
		Questionnaire newQuestionnaire = createNewQuestionnaire(newName, newDesc, questions.size());
		questionnaireDao.add(newQuestionnaire);
		//复制问题
		copyQuestions(newQuestionnaire.getId(), questions);
		//复制问题选项
		copyQuestionOption(sourceQuestionIds, questions);
		return ResponseResult.ok();
	}

	private void copyQuestionOption(List<Long> sourceQuestionIds, List<QuestionnaireQuestion> newQuestions) {
		int i = 0;
		for (Long sourceQuestionId : sourceQuestionIds) {
			List<QuestionnaireQuestionOption> options = questionnaireQuestionOptionDao
					.findByQuestionId(sourceQuestionId);
			QuestionnaireQuestion newQuestion = newQuestions.get(i++);
			for (QuestionnaireQuestionOption option : options) {
				option.setId(null);
				option.setQuestionId(newQuestion.getId());
				questionnaireQuestionOptionDao.add(option);
			}
		}
	}

	private void copyQuestions(long newQuestionnaireId, List<QuestionnaireQuestion> questions) {
		for (QuestionnaireQuestion question : questions) {
			question.setId(null);
			question.setQuestionnaireId(newQuestionnaireId);
			questionnaireQuestionDao.add(question);
		}
	}

	private Questionnaire createNewQuestionnaire(String newName, String newGreeting, int questionCount) {
		Questionnaire newQuestionnaire = new Questionnaire();
		newQuestionnaire.setName(newName);
		newQuestionnaire.setQuestionCount(questionCount);
		newQuestionnaire.setGreeting(newGreeting);
		newQuestionnaire.setCreateDate(new Date());
		newQuestionnaire.setUserId(LoginUserService.getLoginUser().getId());
		newQuestionnaire.setStatus(QuestionnaireStatusEnum.DESIGN.intValue());
		newQuestionnaire.setInvokeCount(0);
		return newQuestionnaire;
	}
}
