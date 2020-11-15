package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author geng
 * 2020/11/1
 */

@Repository
@Mapper
public interface UserDao {

    @Select("select id,username,real_name as realName,phone" +
            ",email,age,sex,password,password_salt as passwordSalt,is_vip as isVip" +
            ",create_date as createDate from user")
    List<User> findAll();

    @Select("select id,username,real_name as realName,phone" +
            ",email,age,sex,password,password_salt as passwordSalt,is_vip as isVip" +
            ",create_date as createDate from user where username=#{username}")
    List<User> findByUsername(String username);

    @Select("select count(username) from user where username=#{username}")
    int countByUsername(@Param("username") String username);

    @Insert("insert into " +
            "user(username,real_name,phone,email,age,sex,password,password_salt,is_vip,create_date)" +
            " value(#{username},#{realName},#{phone},#{email},#{age},#{sex},#{password},#{passwordSalt},#{isVip},#{createDate})")
    void addUser(User user);
}
