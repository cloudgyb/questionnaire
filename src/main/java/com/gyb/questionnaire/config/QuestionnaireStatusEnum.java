package com.gyb.questionnaire.config;

/**
 * 调查问卷状态枚举
 * @author cloudgyb
 * 2021/4/7 10:45
 */
public enum QuestionnaireStatusEnum {

	DESIGN(0),
	PUBLISHED(1),
	FINISHED(2);

	private final int status;

	QuestionnaireStatusEnum(int status){
		this.status = status;
	}

	public int intValue() {
		return status;
	}
}
