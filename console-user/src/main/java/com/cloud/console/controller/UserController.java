package com.cloud.console.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.console.po.User;
import com.cloud.console.service.Paging;
import com.cloud.console.service.UserService;
import com.cloud.console.vo.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/8. */
@RestController
@Slf4j(topic = "app")
public class UserController {

  @Autowired RedisTemplate redisTemplate;
  @Autowired UserService userService;

  @GetMapping("/getUsers")
  @PreAuthorize("authenticated and hasPermission(3, 'query')")
  public Paging getUsers(
      Integer limit,
      Integer offset,
      String sortName,
      String sortOrder,
      String username,
      String startTime,
      String endTime)
      throws Exception {
    return userService.getUsers(limit, offset, sortName, sortOrder, username, startTime, endTime);
  }

  @GetMapping("/getUser")
  public User getUser(@RequestBody com.cloud.console.vo.User params) {
    User user = userService.getUser(params);
    user.setPassword(null);
    return user;
  }

  @GetMapping("getUserRoles")
  public List<UserRole> getUserRoles(String param) {
    return userService.getUserRoles(param);
  }

  @GetMapping(value = "/isExist/{userName}")
  public Map<String, Object> isExist(@PathVariable String userName) {
    com.cloud.console.vo.User params = new com.cloud.console.vo.User();
    params.setUsername(userName);
    Map<String, Object> result = new HashMap<>();
    User user = userService.getUser(params);
    if (user != null) {
      result.put("code", 0);
      result.put("msg", "帐号已存在");
    } else {
      result.put("code", 1);
      result.put("msg", "notExist");
    }
    return result;
  }

  @PostMapping(value = "/register")
  public Map<String, Object> register(User user) throws Exception {
    Map<String, Object> result = new HashMap<>();
    if (user != null
        && StringUtils.isNotBlank(user.getUsername())
        && StringUtils.isNotBlank(user.getPassword())) {
      userService.insertUser(user);
      result.put("code", 1);
      result.put("msg", "注册成功");
    } else {
      result.put("code", 1);
      result.put("msg", "注册失败，用户资料不全");
    }
    this.log(user.getUsername() + "注册");
    return result;
  }

  @PatchMapping("/update")
  @PreAuthorize("authenticated and hasPermission(3, 'update')")
  public Integer edit(User user) throws Exception {
    if (user != null && StringUtils.isNotBlank(user.getUsername())) {
      userService.updateUser(user);
    }
    this.log("编辑用户" + JSON.toJSONString(user));
    return 1;
  }

  @DeleteMapping("/del")
  @PreAuthorize("authenticated and hasPermission(3, 'del')")
  public Integer del(@RequestBody List<User> users) throws Exception {
    userService.delUser(users);
    this.log("删除用户" + JSON.toJSONString(users));
    return 1;
  }

  @PostMapping("/author")
  @PreAuthorize("authenticated and hasPermission(3, 'auth')")
  public Integer auth(Long userId, String roles) throws Exception {
    userService.auth(userId, roles);
    this.log("授予" + userId + "角色" + roles);
    return 1;
  }

  @PostMapping("/uploadImg")
  public Integer uploadImg(HttpServletRequest request) throws Exception {
    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    MultipartFile multipartFile = null;
    Map map = multipartRequest.getFileMap();
    for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
      Object obj = i.next();
      multipartFile = (MultipartFile) map.get(obj);
    }
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String filePath = this.getClass().getResource("/").getPath() + "static/avatars/";
    userService.saveImg(filePath, userName, multipartFile);
    return 1;
  }

  private void log(String text) {
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    log.info(user != null ? user.getUsername() : "" + text);
  }
}
