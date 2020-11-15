package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.QuestionnaireType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调查问卷模板类型（分类）Dao
 *
 * @author geng
 * 2020/11/8
 */

@Mapper
@Repository
public interface QuestionnaireTemplateTypeDao {

    @Select("select id,type_name as typeName,type_desc as typeDesc from questionnaire_type " +
            "where id=#{id}")
    QuestionnaireType find(long id);

    @Select("select id,type_name as typeName,type_desc as typeDesc from questionnaire_type")
    List<QuestionnaireType> findAll();
}
