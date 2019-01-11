package com.cloud.console.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Created by Frank on 2019-01-11. */
@Component
@Slf4j(topic = "manage")
public class Logger {
  public void log(String text) {
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    // User user = userOperations.get(userName);
    log.info(userName + text);
  }
}
