package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.PaperAnswer;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 问卷答卷问题答案dao
 *
 * @author geng
 * 2020/11/21
 */
@Mapper
@Repository
public interface PaperAnswerDao {
    @Insert("insert into paper_answer(" +
            "id,paper_id,question_id,answer) " +
            "value(#{id},#{paperId},#{questionId},#{answer})")
    int insert(PaperAnswer pa);

    @Update("update paper_answer set " +
            "paper_id=#{paperId}," +
            "question_id=#{questionId}," +
            "answer=#{answer} where id=#{id}")
    int update(PaperAnswer p);

    @Delete("delete from paper_answer where id=#{id}")
    int delete(String id);

    @Delete("delete from paper_answer where paper_id=#{paperId}")
    int deleteByPaperId(String paperId);

    @Delete("delete from paper_answer where question_id=#{questionId}")
    int deleteByQuestionId(String questionId);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where id=#{id}")
    PaperAnswer find(String id);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where paper_id=#{paperId}")
    List<PaperAnswer> findByPaperId(String paperId);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where question_id=#{questionId}")
    List<PaperAnswer> findByQuestionId(String questionId);

    @Select("select answer from paper_answer where question_id=#{questionId} limit ${limit}")
    List<String> findAnswerByQuestionId(String questionId,int limit);

    @Select("select id,paper_id as paperId," +
            "question_id as questionId," +
            "answer from paper_answer where paper_id=#{paperId} and question_id=#{questionId}")
    PaperAnswer findByPaperIdAndQuestionId(String paperId, String questionId);

    @Select("select answer from paper_answer where paper_id=#{paperId} and question_id=#{questionId}")
    String findAnswerByPaperIdAndQuestionId(String paperId, String questionId);
}
