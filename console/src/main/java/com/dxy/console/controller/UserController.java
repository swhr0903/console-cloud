package com.dxy.console.controller;

import com.alibaba.fastjson.JSON;
import com.dxy.console.po.User;
import com.dxy.console.service.Paging;
import com.dxy.console.service.UserService;
import com.dxy.console.vo.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/8.
 */
@Controller
@RequestMapping(value = "/user")
@Slf4j(topic = "app")
public class UserController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/all")
    @PreAuthorize("authenticated and hasPermission(3, 'all')")
    public String users(Model model) throws Exception {
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

    @RequestMapping(value = "/getUsers")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(3, 'query')")
    public Paging getUsers(Integer limit, Integer offset, String sortName, String sortOrder,
                           String username, String startTime, String endTime) throws Exception {
        return userService.getUsers(limit, offset, sortName, sortOrder, username, startTime, endTime);
    }

    @RequestMapping(value = "/getUser")
    @ResponseBody
    public User getUser(@RequestBody com.dxy.console.vo.User params) throws Exception {
        User user = userService.getUser(params);
        user.setPassword(null);
        return user;
    }

    @RequestMapping(value = "getUserRoles")
    @ResponseBody
    public List<UserRole> getUserRoles(String param) throws Exception {
        return userService.getUserRoles(param);
    }

    @RequestMapping(value = "/isExist")
    @ResponseBody
    public Map<String, Object> isExist(@RequestBody com.dxy.console.vo.User params) throws Exception {
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

    @RequestMapping(value = "/register")
    public String register(User user) throws Exception {
        if (user != null && StringUtils.isNotBlank(user.getUsername())) {
            userService.insertUser(user);
        }
        this.log(user.getUsername() + "注册");
        return "login";
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(3, 'update')")
    public Integer edit(@RequestBody User user) throws Exception {
        if (user != null && StringUtils.isNotBlank(user.getUsername())) {
            userService.updateUser(user);
        }
        this.log("编辑用户" + JSON.toJSONString(user));
        return 1;
    }

    @RequestMapping(value = "/del")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(3, 'del')")
    public Integer del(@RequestBody List<User> users) throws Exception {
        userService.delUser(users);
        this.log("删除用户" + JSON.toJSONString(users));
        return 1;
    }

    @RequestMapping(value = "/auth")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(3, 'auth')")
    public Integer auth(Long userId, String roles) throws Exception {
        userService.auth(userId, roles);
        this.log("授予" + userId + "角色" + roles);
        return 1;
    }

    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public Integer uploadImg(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = null;
        Map map = multipartRequest.getFileMap();
        for (Iterator i = map.keySet().iterator(); i.hasNext(); ) {
            Object obj = i.next();
            multipartFile = (MultipartFile) map.get(obj);
        }
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String filePath = this.getClass().getResource("/").getPath() + "static/avatars/";
        userService.saveImg(filePath, userName, multipartFile);
        return 1;
    }

    private void log(String text) throws Exception {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        log.info(user != null ? user.getUsername() : "" + text);
    }
}
