package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.TemplateQuestion;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 模板问题dao
 *
 * @author geng
 * 2020/11/9
 */
@Repository
@Mapper
public interface TemplateQuestionDao {
    @Select("select id, template_id as templateId, question_type as questionType," +
            "question_order as questionOrder,question_num as questionNum," +
            " question_title as questionTitle, input_placeholder as inputPlaceholder " +
            "from template_question where template_id=#{templateId}")
    List<TemplateQuestion> getByTemplateId(long templateId);

    @Delete("delete from template_question where template_id=#{templateId}")
    int deleteByTemplateId(long templateId); //根据问题模板id删除

    @Select("select id," +
            "template_id as templateId," +
            "question_type as questionType," +
            "question_order as questionOrder," +
            "question_num as questionNum," +
            "question_title as questionTitle," +
            "input_placeholder as inputPlaceholder " +
            "from template_question where id=#{id}")
    TemplateQuestion get(int id);

    @Update("update template_question set " +
            "template_id=#{templateId},question_type=#{questionType}," +
            "question_order=#{questionOrder}," +
            "question_num=#{questionNum},question_title=#{questionTitle}," +
            "input_placeholder=#{inputPlaceholder} where id=#{id}")
    int update(TemplateQuestion q);

    @Delete("delete from template_question where id=#{id}")
    void delete(long id);
    @Insert("insert into template_question(" +
            "template_id,question_type,question_order,question_num," +
            "question_title,input_placeholder) " +
            "values(#{templateId},#{questionType},#{questionOrder}," +
            "#{questionNum},#{questionTitle},#{inputPlaceholder})")
    int add(TemplateQuestion templateQuestion);
}