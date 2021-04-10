package com.gyb.questionnaire.service;

import com.gyb.questionnaire.dao.QuestionnaireDao;
import com.gyb.questionnaire.entity.Questionnaire;
import com.gyb.questionnaire.entity.User;
import com.gyb.questionnaire.util.HttpServletUtil;

import static com.gyb.questionnaire.config.GlobalConstant.SESSION_KEY_CURR_USER;

/**
 * @author cloudgyb
 * 2021/4/7 10:28
 */
public abstract class QuestionnaireBaseService {
	private final QuestionnaireDao questionnaireDao;

	protected QuestionnaireBaseService(QuestionnaireDao questionnaireDao) {
		this.questionnaireDao = questionnaireDao;
	}

	/**
	 * 根据id获取问卷
	 *
	 * @param questionnaireId 问卷id
	 */
	public Questionnaire get(long questionnaireId) {
		return questionnaireDao.find(questionnaireId);
	}

	/**
	 * 根据问卷id获取登录用户的问卷，如果该登陆用户下没有对应的问卷返回null
	 *
	 * @param questionnaireId 问卷id
	 */
	public Questionnaire getUserQuestionnaire(Long questionnaireId) {
		final User u = (User) (HttpServletUtil.getSession().getAttribute(SESSION_KEY_CURR_USER));
		if (u == null)
			return null;
		return questionnaireDao.findByIdAndUserId(questionnaireId, u.getId());
	}
}
