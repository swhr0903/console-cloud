package com.cloud.console.service;

import com.cloud.console.mapper.UserMapper;
import com.cloud.console.po.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Created by Frank on 2017/8/3. */
@Service
public class UserServiceImpl implements UserService {

  @Autowired RedisTemplate redisTemplate;
  @Autowired UserMapper userMapper;

  @Override
  public User getUser(com.cloud.console.vo.User user) {
    return userMapper.getUser(user);
  }

  @Override
  @Transactional
  public void updateUser(User user) {
    String password = user.getPassword();
    if (StringUtils.isNotBlank(password)) {
      user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    }
    userMapper.updateUser(user);
  }
}
