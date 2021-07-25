package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.Template;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 调查问卷模板dao
 *
 * @author geng
 * 2020/11/8
 */

@Mapper
@Repository
public interface TemplateDao {

    @Select("select t.id,name,t.create_date as createDate," +
            "publish_date as publishDate,t.author_id as authorId,u.username as authorName," +
            "type_id as typeId,question_count as questionCount,t.status as status " +
            "from template as t left join user as u on t.author_id=u.id where t.status=1")
    List<Template> findAll();

    @Select("select t.id,name,t.create_date as createDate," +
            "publish_date as publishDate,t.author_id as authorId,u.username as authorName," +
            "type_id as typeId,question_count as questionCount,t.status as status " +
            "from template as t left join user as u on t.author_id=u.id " +
            "where t.type_id=#{typeId} and t.status=1")
    List<Template> findByTypeId(long typeId);
    @Select("select t.id,name,t.create_date as createDate," +
            "publish_date as publishDate,t.author_id as authorId,u.username as authorName," +
            "type_id as typeId,question_count as questionCount,t.status as status " +
            "from template as t left join user as u on t.author_id=u.id " +
            "where t.id=#{id} and t.status=1")
    Template find(long id);
}
