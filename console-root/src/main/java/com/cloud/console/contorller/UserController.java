package com.cloud.console.contorller;

import com.cloud.console.po.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/** Created by Frank on 2018-12-11. */
@Controller
public class UserController {

  @Autowired RedisTemplate redisTemplate;

  @RequestMapping("/allUser")
  @PreAuthorize("authenticated and hasPermission(3, 'all')")
  public String users(Model model) {
    ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
    String menus = StringOperations.get("menus");
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (StringUtils.isBlank(userName)) {
      return "login";
    }
    model.addAttribute("user", userName);
    List<GrantedAuthority> grantedAuthorities =
        (List<GrantedAuthority>)
            SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    StringBuilder roles = new StringBuilder();
    for (int i = 0; i < grantedAuthorities.size(); i++) {
      GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
      roles
          .append(grantedAuthority.getAuthority().split("_")[1])
          .append(i < grantedAuthorities.size() - 1 ? "," : "");
    }
    if ("admin".equals(userName)) {
      model.addAttribute("role", "ADMIN");
    } else {
      model.addAttribute(
          "role", StringUtils.isNotBlank(roles.toString()) ? roles.toString() : "NEWBIE");
    }
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    model.addAttribute("page", "user.ftl");
    String avatar = user.getAvatar();
    if (StringUtils.isNotBlank(avatar)) {
      model.addAttribute("avatar", avatar);
    }
    model.addAttribute("createTime", user.getCreate_time());
    model.addAttribute("menus", menus);
    model.addAttribute("title", "用户信息管理");
    model.addAttribute("summary", "系统用户数据");
    return "index";
  }
}
