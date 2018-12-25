package com.cloud.console.mapper;

import com.cloud.console.po.Dict;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/23. */
public interface DictMapper {

  @Select(
      "<script>select id,dict_name,dict_value,status from t_dictionary"
          + "<if test=\"_parameter!=null\">limit #{_parameter.offset},#{_parameter.limit}</if>"
          + "</script>")
  List<Dict> getDicts(Map<String, Integer> params);

  @Select("select id,dict_name,dict_value,status from t_dictionary where dict_name like concat('%',#{name},'%')")
  List<Dict> getDictByName(String name);

  @Select(
      "<script>"
          + "select count(1) from t_dictionary "
          + "<if test=\"_parameter!=null and _parameter!=''\"> where dict_name like concat('%',#{name},'%')</if>"
          + "</script>")
  Integer getDictCount(String name);

  @Insert(
      "insert into t_dictionary(dict_name,dict_value,status) values(#{dict_name},#{dict_value},#{status})")
  void insertDict(Dict dict);

  @Update(
      "<script>"
          + "update t_dictionary set "
          + "<if test=\"dict_name!=null and dict_name!=''\">dict_name=#{dict_name},</if>"
          + "<if test=\"dict_value!=null and dict_value!=''\">dict_value=#{dict_value},</if>"
          + "<if test=\"status!=null\">status=#{status}</if>"
          + " where id=#{id}"
          + "</script>")
  void updateDict(Dict dict);

  @Delete(
      "<script>"
          + "delete from t_dictionary where id in "
          + "<if test=\"_parameter!=null\">"
          + "<foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">"
          + "#{item.id}"
          + "</foreach></if>"
          + "</script>")
  void delDict(List<Dict> dicts);
}
