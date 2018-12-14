package com.cloud.console.service;

import com.cloud.console.po.Module;
import com.cloud.console.vo.TreeNode;

import java.util.List;

/** Created by Frank on 2017/8/3. */
public interface ModuleService {

  /**
   * 获取所有模块
   *
   * @return
   */
  Paging getModules(Integer limit, Integer offset, String name) throws Exception;

  /**
   * 根据名称获得模块
   *
   * @param name
   * @return
   */
  List<com.cloud.console.vo.Module> getModuleByName(String name) throws Exception;

  /**
   * 新增模块
   *
   * @param module
   * @throws Exception
   */
  void addModule(Module module) throws Exception;

  /**
   * 更新模块
   *
   * @param module
   * @throws Exception
   */
  void updateModule(Module module) throws Exception;

  /**
   * 删除模块
   *
   * @param modules
   */
  void delModule(List<Module> modules) throws Exception;

  /**
   * 构造树形菜单
   *
   * @return
   */
  TreeNode buildTree() throws Exception;
}
