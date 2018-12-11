package com.cloud.console.service;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
public interface SecurityService {

  /**
   * 获取用户
   *
   * @param user
   * @return
   */
  User getUser(com.cloud.console.vo.User user);

  /**
   * 获取用户角色
   *
   * @param param
   * @return
   */
  List<UserRole> getUserRoles(String param);

  /**
   * 验证用户权限
   *
   * @param authObj
   * @return
   */
  Boolean authorized(Map<String, Object> authObj);
}
