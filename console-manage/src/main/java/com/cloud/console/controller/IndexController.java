package com.cloud.console.controller;

import com.cloud.console.service.IndexService;
import com.cloud.console.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@RestController
public class IndexController {

  @Autowired UserService userService;
  @Autowired IndexService indexService;

  @GetMapping("getDWCount")
  public List<Map<Integer, Integer>> getDWCount() throws Throwable {
    return indexService.getDWCount();
  }

  @GetMapping(value = "/regiUserCount7")
  public Map<String, List<Integer>> regiUserCount7() {
    Map<String, List<Integer>> stats = new HashMap();
    stats.put("regiUserCount7", userService.regiUserCount7());
    stats.put("regiDepositCount7", userService.regiDepositCount7());
    return stats;
  }

  @GetMapping(value = "/depositWCount7")
  public Map<String, List<Integer>> depositWCount7() {
    Map<String, List<Integer>> stats = new HashMap();
    stats.put("depositSum7", userService.depositSum7());
    stats.put("withdrawSum7", userService.withdrawSum7());
    return stats;
  }
}
