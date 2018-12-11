package com.cloud.console.mapper;

import com.cloud.console.po.Role;
import com.cloud.console.po.RoleAuth;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/23. */
public interface RoleMapper {

  @Select(
      "<script>"
          + "select id,code,name,status from t_role where 1=1 "
          + "<if test=\"_parameter.code!=null and _parameter.code!=''\">and code = #{code}</if>"
          + "<if test=\"_parameter.name!=null and _parameter.name!=''\">and name like concat('%',#{name},'%')</if>"
          + "<if test=\"_parameter.offset!=null\">limit #{_parameter.offset},#{_parameter.limit}</if>"
          + "</script>")
  List<Role> getRoles(Map<String, Object> paging);

  @Select(
      "<script>"
          + "select count(1) from t_role where 1=1"
          + "<if test=\"_parameter.code!=null and _parameter.code!=''\">and code = #{code}</if>"
          + "<if test=\"_parameter.name!=null and _parameter.name!=''\">and name like concat('%',#{name},'%')</if>"
          + "</script>")
  Integer getRoleCount(Map<String, Object> params);

  @Insert("insert into t_role(code,name,status) values(#{code},#{name},#{status})")
  void insertRole(Role role);

  @Insert(
      "<script>"
          + "insert into t_role_auth(role_id,module_id,permission) values"
          + "<foreach collection=\"list\" item=\"roleAuth\" separator=\",\">"
          + "(#{roleAuth.role_id},#{roleAuth.module_id},#{roleAuth.permission})"
          + "</foreach>"
          + "</script>")
  void batchInsertRoleAuth(List<RoleAuth> roleAuths);

  @Update(
      "<script>"
          + "update t_role set "
          + "<if test=\"code!=null and code!=''\">code=#{code},</if>"
          + "<if test=\"name!=null\">name=#{name},</if>"
          + "<if test=\"status!=null and status!=''\">status=#{status}</if>"
          + " where id=#{id}"
          + "</script>")
  void updateRole(Role role);

  @Delete(
      "<script>"
          + "delete from t_role where id in "
          + "<if test=\"_parameter!=null\">"
          + "<foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">"
          + "#{item.id}"
          + "</foreach></if>"
          + "</script>")
  void delRole(List<Role> roles);

  @Delete(
      "<script>"
          + "delete from t_role_auth where 1=1 "
          + "<if test=\"_parameter.role_id!=null\">"
          + "and role_id=#{role_id}"
          + "</if>"
          + "</script>")
  void delRoleAuth(RoleAuth roleAuth);

  @Select("select module_id,permission from t_role_auth where role_id=#{_parameter}")
  List<RoleAuth> getRoleAuths(Long roleId);
}
