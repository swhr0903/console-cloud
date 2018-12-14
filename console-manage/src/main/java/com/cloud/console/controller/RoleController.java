package com.cloud.console.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.console.po.Role;
import com.cloud.console.po.RoleAuth;
import com.cloud.console.po.User;
import com.cloud.console.service.IndexService;
import com.cloud.console.service.Paging;
import com.cloud.console.service.RoleService;
import com.cloud.console.vo.Auths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@RestController
@RequestMapping(value = "/role")
@Slf4j(topic = "app")
public class RoleController {

  @Autowired RedisTemplate redisTemplate;
  @Autowired RoleService roleService;
  @Autowired IndexService indexService;

  @GetMapping(value = "/isExist")
  public Map<String, Object> isExist(Role params) throws Exception {
    Map<String, Object> result = new HashMap<>();
    List<Role> role = roleService.getRoles(null, null, params.getCode(), null).getRows();
    if (role != null && role.size() > 0) {
      result.put("code", 0);
      result.put("msg", "角色已存在");
    } else {
      result.put("code", 1);
      result.put("msg", "notExist");
    }
    return result;
  }

  @GetMapping(value = "/getRolesPaging")
  @PreAuthorize("authenticated and hasPermission(4, 'query')")
  public Paging getRolesPaging(Integer limit, Integer offset, String code, String name)
      throws Exception {
    return roleService.getRoles(limit, offset, code, name);
  }

  @GetMapping(value = "/getRoles")
  public List<Role> getRoles() throws Exception {
    Paging paging = roleService.getRoles(null, null, null, null);
    return paging.getRows();
  }

  @PostMapping(value = "/add")
  @PreAuthorize("authenticated and hasPermission(4, 'add')")
  public Integer add(Role role) throws Exception {
    roleService.addRole(role);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("编辑角色" + JSON.toJSONString(role));
    return 1;
  }

  @PatchMapping(value = "/update")
  @PreAuthorize("authenticated and hasPermission(4, 'update')")
  public Integer update(Role role) throws Exception {
    roleService.updateRole(role);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("编辑角色" + JSON.toJSONString(role));
    return 1;
  }

  @DeleteMapping(value = "/del")
  @PreAuthorize("authenticated and hasPermission(4, 'del')")
  public Integer del(@RequestBody List<Role> roles) throws Exception {
    roleService.delRole(roles);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("删除角色" + JSON.toJSONString(roles));
    return 1;
  }

  @PostMapping(value = "/auth")
  @PreAuthorize("authenticated and hasPermission(4, 'auth')")
  public Map<String, Object> auth(@RequestBody Auths auths) throws Exception {
    Map<String, Object> result = new HashMap<>();
    roleService.auth(auths);
    this.log("角色授权" + JSON.toJSONString(auths.getPermissions()));
    result.put("code", 1);
    result.put("msg", "角色授权成功");
    return result;
  }

  @RequestMapping(value = "/getRoleAuths")
  @ResponseBody
  public List<RoleAuth> getRoleAuths(Long roleId) throws Exception {
    return roleService.getRoleAuths(roleId);
  }

  private void log(String text) throws Exception {
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    log.info(user.getUsername() + text);
  }
}
