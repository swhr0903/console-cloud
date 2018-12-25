package com.cloud.console.service;

import com.cloud.console.po.Dict;

import java.util.List;

/** Created by Frank on 2017/8/3. */
public interface DictService {

  /**
   * 获取所有字段
   *
   * @return
   */
  Paging getDicts(Integer limit, Integer offset, String name) throws Exception;

  /**
   * 根据名称获得字段
   *
   * @param name
   * @return
   */
  List<Dict> getDictByName(String name) throws Exception;

  /**
   * 新增字段
   *
   * @param dict
   * @throws Exception
   */
  void addDict(Dict dict) throws Exception;

  /**
   * 更新字段
   *
   * @param dict
   * @throws Exception
   */
  void updateDict(Dict dict) throws Exception;

  /**
   * 删除字段
   *
   * @param dicts
   */
  void delDict(List<Dict> dicts) throws Exception;
}
