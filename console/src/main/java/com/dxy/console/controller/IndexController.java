package com.dxy.console.controller;

import com.dxy.console.po.User;
import com.dxy.console.service.IndexService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
@Controller
public class IndexController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/")
    public String index(Model model) throws Exception {
        ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
        String menus = StringOperations.get("menus");
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(userName)) {
            return "login";
        }
        model.addAttribute("user", userName);
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        StringBuilder roles = new StringBuilder();
        for (int i = 0; i < grantedAuthorities.size(); i++) {
            GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
            roles.append(grantedAuthority.getAuthority().split("_")[1])
                    .append(i < grantedAuthorities.size() - 1 ? "," : "");
        }
        if ("admin".equals(userName)) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", StringUtils.isNotBlank(roles.toString()) ? roles.toString() : "NEWBIE");
        }
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        model.addAttribute("page", "welcome.ftl");
        String avatar = user.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            model.addAttribute("avatar", avatar);
        }
        model.addAttribute("createTime", user.getCreate_time());
        model.addAttribute("menus", menus);
        model.addAttribute("title", "欢迎");
        model.addAttribute("summary", "生活从这里开始");
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login() throws Exception {
        return "login";
    }

    @RequestMapping(value = "/403")
    public String accessDenied(Model model) throws Exception {
        ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
        String menus = StringOperations.get("menus");
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(userName)) {
            return "login";
        }
        model.addAttribute("user", userName);
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        StringBuilder roles = new StringBuilder();
        for (int i = 0; i < grantedAuthorities.size(); i++) {
            GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
            roles.append(grantedAuthority.getAuthority().split("_")[1])
                    .append(i < grantedAuthorities.size() - 1 ? "," : "");
        }
        if (StringUtils.isNotBlank(roles.toString())) {
            model.addAttribute("role", roles.toString());
        } else {
            model.addAttribute("role", "NEWBIE");
        }
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        model.addAttribute("avatar", user.getAvatar());
        model.addAttribute("createTime", user.getCreate_time());
        model.addAttribute("menus", menus);
        return "403";
    }

    @RequestMapping(value = "/getDWCount")
    public @ResponseBody
    List<Map<Integer, Integer>> getDWCount() throws Exception {
        return indexService.getDWCount();
    }
}
