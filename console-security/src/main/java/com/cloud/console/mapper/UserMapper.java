package com.cloud.console.mapper;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
public interface UserMapper {

  @Select(
      "<script>"
          + "select id,username,password,name,email,create_time,avatar,status,mfa_secret from t_user where 1=1"
          + "<if test=\"_parameter.username!=null and _parameter.username!=''\">and username=#{_parameter.username}</if>"
          + "<if test=\"_parameter.status!=null\">and status=#{_parameter.status}</if>"
          + "<if test=\"_parameter.password!=null and _parameter.password!=''\">and password=#{_parameter.password}</if>"
          + "</script>")
  User getUser(com.cloud.console.vo.User user);

  @Select(
      "select b.id,b.user_id,b.role_id,c.code as role_code,c.name as role_name from t_user a "
          + "inner join t_user_role b on a.id=b.user_id inner join t_role c on b.role_id=c.id "
          + "where (a.id=#{param} || a.username=#{param}) and c.status=1")
  List<UserRole> getUserRoles(String param);

  @Select(
      "<script>"
          + "select count(1) from t_role_auth a inner join t_user_role b on a.role_id=b.role_id where 1=1"
          + "<if test=\"_parameter.userId!=null\">and b.user_id=#{_parameter.userId}</if>"
          + "<if test=\"_parameter.moduleId!=null\">and a.module_id=#{_parameter.moduleId}</if>"
          + "<if test=\"_parameter.permission!=null and _parameter.permission!=''\">and a.permission "
          + "like concat('%',#{_parameter.permission},'%')</if>"
          + "</script>")
  Integer getUserAuths(Map<String, Object> params);

  @Update(
      "<script>"
          + "update t_user <set> "
          + "<if test=\"username!=null and username!=''\">username=#{username},</if>"
          + "<if test=\"name!=null and name!=''\">name=#{name},</if>"
          + "<if test=\"password!=null and password!=''\">password=#{password},</if>"
          + "<if test=\"email!=null and email!=''\">email=#{email},</if>"
          + "<if test=\"mfa_secret!=null and mfa_secret!=''\">mfa_secret=#{mfa_secret},</if>"
          + "<if test=\"avatar!=null and avatar!=''\">avatar=#{avatar}</if></set>"
          + " where <if test=\"id!=null\">id=#{id}</if>"
          + "<if test=\"id==null and username!=null and username!=''\">username=#{username}</if></script>")
  void updateUser(User user);
}
