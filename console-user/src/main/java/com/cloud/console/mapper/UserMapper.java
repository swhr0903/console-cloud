package com.cloud.console.mapper;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
public interface UserMapper {

    @Select("<script>" +
            "select id,username,password,name,email,create_time,avatar from t_user where 1=1" +
            "<if test=\"_parameter.username!=null and _parameter.username!=''\"> and username=#{_parameter.username}</if>" +
            "<if test=\"_parameter.status!=null\">and status=#{_parameter.status}</if>" +
            "<if test=\"_parameter.startTime!=null and _parameter.startTime!=''\"> and create_time<![CDATA[>=]]>#{_parameter.startTime}</if>" +
            "<if test=\"_parameter.endTime!=null and _parameter.endTime!=''\"> and create_time<![CDATA[<=]]>#{_parameter.endTime}</if>" +
            "<if test=\"_parameter.sort!=null and _parameter.sort!=''\"> order by ${_parameter.sort}</if>" +
            "<if test=\"_parameter.offset!=null \"> limit #{_parameter.offset}</if>" +
            "<if test=\"_parameter.limit!=null \">,#{_parameter.limit}</if>" +
            "</script>")
    List<User> getUsers(Map<String, Object> paging);

    @Select("<script>" +
            "select count(1) from t_user where 1=1" +
            "<if test=\"param1!=null and param1!=''\">and username=#{param1}</if>" +
            "<if test=\"param2!=null and param2!=''\">and create_time<![CDATA[>=]]>#{param2}</if>" +
            "<if test=\"param3!=null and param3!=''\">and create_time<![CDATA[<=]]>#{param3}</if>" +
            "</script>")
    Integer getUserCount(String username, String startTime, String endTime);

    @Select("<script>" +
            "select id,username,password,name,email,create_time,avatar,status,mfa_secret from t_user where 1=1" +
            "<if test=\"_parameter.username!=null and _parameter.username!=''\">and username=#{_parameter.username}</if>" +
            "<if test=\"_parameter.status!=null\">and status=#{_parameter.status}</if>" +
            "<if test=\"_parameter.password!=null and _parameter.password!=''\">and password=#{_parameter.password}</if>" +
            "</script>")
    User getUser(com.cloud.console.vo.User user);

    @Select("select b.id,b.user_id,b.role_id,c.code as role_code,c.name as role_name from t_user a " +
            "inner join t_user_role b on a.id=b.user_id inner join t_role c on b.role_id=c.id " +
            "where (a.id=#{param} || a.username=#{param}) and c.status=1")
    List<UserRole> getUserRoles(String param);

    @Insert("insert into t_user(username,password,name,email,create_time,status) values(#{username},#{password}" +
            ",#{name},#{email},now(),0)")
    void insertUser(User user);

    @Update("<script>" +
            "update t_user <set> " +
            "<if test=\"username!=null and username!=''\">username=#{username},</if>" +
            "<if test=\"name!=null and name!=''\">name=#{name},</if>" +
            "<if test=\"password!=null and password!=''\">password=#{password},</if>" +
            "<if test=\"email!=null and email!=''\">email=#{email},</if>" +
            "<if test=\"mfa_secret!=null and mfa_secret!=''\">mfa_secret=#{mfa_secret},</if>" +
            "<if test=\"avatar!=null and avatar!=''\">avatar=#{avatar}</if></set>" +
            " where <if test=\"id!=null\">id=#{id}</if>" +
            "<if test=\"id==null and username!=null and username!=''\">username=#{username}</if></script>")
    void updateUser(User user);

    @Delete("<script>" +
            "delete from t_user where id in " +
            "<if test=\"_parameter!=null\">" +
            "<foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">" +
            "#{item.id}" +
            "</foreach></if>" +
            "</script>")
    void delUser(List<User> users);

    @Insert("<script>" +
            "insert into t_user_role(user_id,role_id) values" +
            "<foreach collection=\"list\" item=\"userRole\" separator=\",\">" +
            "(#{userRole.user_id},#{userRole.role_id})" +
            "</foreach>" +
            "</script>")
    void batchInsertUserRole(List<UserRole> userRoles);

    @Delete("<script>" +
            "delete from t_user_role where 1=1 " +
            "<if test=\"_parameter.user_id!=null\">" +
            "and user_id=#{user_id}" +
            "</if>" +
            "</script>")
    void delUsereRole(UserRole userRole);
}
