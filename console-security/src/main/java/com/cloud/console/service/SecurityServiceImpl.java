package com.cloud.console.service;

import com.cloud.console.mapper.UserMapper;
import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@Service
public class SecurityServiceImpl implements SecurityService {

  @Autowired RedisTemplate redisTemplate;
  @Autowired UserMapper userMapper;

  @Override
  public User getUser(com.cloud.console.vo.User user) {
    return userMapper.getUser(user);
  }

  @Override
  public List<UserRole> getUserRoles(String param) {
    return userMapper.getUserRoles(param);
  }

  @Override
  public Boolean authorized(Map<String, Object> authObj) {
    Integer roleAuths = userMapper.getUserAuths(authObj);
    if (roleAuths > 0) {
      return true;
    }
    return false;
  }
}
