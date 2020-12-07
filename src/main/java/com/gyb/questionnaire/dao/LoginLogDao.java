package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.LoginLog;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 登录日志Dao
 *
 * @author geng
 * 2020/12/6
 */
@Repository
@Mapper
public interface LoginLogDao {
    @Insert("insert into log_login(id,user_id,client_name,system_name,ip,position,create_time) " +
            "value(#{id},#{userId},#{clientName},#{systemName},#{ip},#{position},#{createTime})")
    int add(LoginLog log);

    @Select("select id,user_id as userId,client_name as clientName," +
            "system_name as systemName,ip,position," +
            "create_time as createTime from log_login where id=#{id}")
    LoginLog find(long id);

    @Select("select id,user_id as userId,client_name as clientName," +
            "system_name as systemName,ip,position," +
            "create_time as createTime from log_login order by create_time desc")
    List<LoginLog> findAll();

    @Select("select id,user_id as userId,client_name as clientName," +
            "system_name as systemName,ip,position," +
            "create_time as createTime from log_login where user_id=#{userId} order by create_time desc")
    List<LoginLog> findByUserId(long userId);

    @Delete("delete from log_login")
    int deleteAll();

    @Delete("delete from log_login where id=#{id}")
    int delete(long id);

    @Delete("delete from log_login where user_id=#{userId}")
    int deleteByUserId(long userId);

    @Delete("delete from log_login where id in(${ids})")
    int deleteByIds(String ids);
}
