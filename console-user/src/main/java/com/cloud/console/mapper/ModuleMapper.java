package com.cloud.console.mapper;

import com.cloud.console.po.Module;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/23. */
public interface ModuleMapper {

  @Select(
      "<script>"
          + "select a.id,a.name,a.url,b.name as parent,a.is_leaf,a.options,a.status from "
          + "t_module a left join t_module b on a.parent_id=b.id "
          + "<if test=\"_parameter!=null\">limit #{_parameter.offset},#{_parameter.limit}</if>"
          + "</script>")
  List<Module> getModules(Map<String, Integer> params);

  @Select(
      "select a.id,a.name,a.url,b.name as parent,a.is_leaf,a.options,a.status from t_module a "
          + "inner join t_module b on a.parent_id=b.id where a.name like concat('%',#{name},'%')")
  List<Module> getModuleByName(String name);

  @Select(
      "<script>"
          + "select count(1) from t_module "
          + "<if test=\"_parameter!=null and _parameter!=''\"> where name=#{_parameter}</if>"
          + "</script>")
  Integer getModuleCount(String name);

  @Insert(
      "insert into t_module(name,url,parent_id,options,is_leaf,status) values(#{name},#{url},"
          + "#{parent_id},#{options},#{is_leaf},#{status})")
  void insertModule(Module module);

  @Update(
      "<script>"
          + "update t_module set "
          + "<if test=\"name!=null\">name=#{name},</if>"
          + "<if test=\"url!=null and url!=''\">url=#{url},"
          + "</if><if test=\"parent_id!=null and parent_id!=''\">parent_id=#{parent_id},</if>"
          + "<if test=\"options!=null and options!=''\">options=#{options},</if>"
          + "<if test=\"is_leaf!=null and is_leaf!=''\">is_leaf=#{is_leaf},</if>"
          + "<if test=\"status!=null and status!=''\">status=#{status}</if>"
          + " where id=#{id}"
          + "</script>")
  void updateModule(Module module);

  @Delete(
      "<script>"
          + "delete from t_module where id in "
          + "<if test=\"_parameter!=null\">"
          + "<foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">"
          + "#{item.id}"
          + "</foreach></if>"
          + "</script>")
  void delModule(List<Module> modules);
}
