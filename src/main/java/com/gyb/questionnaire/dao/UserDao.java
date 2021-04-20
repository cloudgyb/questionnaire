package com.gyb.questionnaire.dao;

import com.gyb.questionnaire.entity.User;
import org.apache.ibatis.annotations.*;
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
            ",create_date as createDate,status from user where id=#{id}")
    User find(long id);

    @Select("select id,username,real_name as realName,phone" +
            ",email,age,sex,password,password_salt as passwordSalt,is_vip as isVip" +
            ",create_date as createDate,status from user")
    List<User> findAll();

    @Select("select id,username,real_name as realName,phone" +
            ",email,age,sex,password,password_salt as passwordSalt,is_vip as isVip" +
            ",create_date as createDate,status from user where username=#{username} and status!=0")
    List<User> findByUsername(String username);

    @Select("select count(username) from user where username=#{username}")
    int countByUsername(@Param("username") String username);

    @Insert("insert into " +
            "user(username,real_name,phone,email,age,sex,"
            + "password,password_salt,is_vip,create_date,status)" +
            " value(#{username},#{realName},#{phone},#{email},#{age},#{sex},"
            + "#{password},#{passwordSalt},#{isVip},#{createDate},#{status})")
    void addUser(User user);

    @Update("update user set " +
            "username=#{username},real_name=#{realName}," +
            "phone=#{phone},email=#{email}," +
            "age=#{age},sex=#{sex}," +
            "password=#{password},password_salt=#{passwordSalt}," +
            "is_vip=#{isVip},status=#{status} " +
            "where id=#{id}")
    int update(User user);

    @Update("update user set password=#{password} where id=#{id}")
    int updatePassword(long id,String password);

    @Update("update user set phone=#{phone} where id=#{id}")
    int updatePhone(long id,String phone);

    @Update("update user set email=#{email} where id=#{id}")
    int updateEmail(long id,String email);
}
