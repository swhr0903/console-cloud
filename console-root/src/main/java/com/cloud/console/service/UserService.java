package com.cloud.console.service;

import com.cloud.console.po.User;

/** Created by Frank on 2017/8/3. */
public interface UserService {

  /**
   * 获取用户
   *
   * @param user
   * @return
   */
  User getUser(com.cloud.console.vo.User user);

  /**
   * 更新用户信息
   *
   * @param user
   */
  void updateUser(User user) throws Exception;
}
