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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
@Controller
@RequestMapping(value = "/role")
@Slf4j(topic = "app")
public class RoleController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RoleService roleService;
    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/all")
    @PreAuthorize("authenticated and hasPermission(4, 'all')")
    public String all(Model model) throws Exception {
        ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
        String menus = StringOperations.get("menus");
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(userName)) {
            return "login";
        }
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
        model.addAttribute("user", userName);
        model.addAttribute("page", "role.ftl");
        String avatar = user.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            model.addAttribute("avatar", avatar);
        }
        model.addAttribute("createTime", user.getCreate_time());
        model.addAttribute("menus", menus);
        model.addAttribute("title", "角色信息管理");
        model.addAttribute("summary", "系统角色数据");
        return "index";
    }

    @RequestMapping(value = "/isExist")
    @ResponseBody
    public Map<String, Object> isExist(@RequestBody Role params) throws Exception {
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

    @RequestMapping(value = "/getRolesPaging")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(4, 'query')")
    public Paging getRolesPaging(Integer limit, Integer offset, String code, String name) throws Exception {
        return roleService.getRoles(limit, offset, code, name);
    }

    @RequestMapping(value = "/getRoles")
    @ResponseBody
    public List<Role> getRoles() throws Exception {
        Paging paging = roleService.getRoles(null, null, null, null);
        return paging.getRows();
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(4, 'add')")
    public Integer add(@RequestBody Role role) throws Exception {
        roleService.addRole(role);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("编辑角色" + JSON.toJSONString(role));
        return 1;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(4, 'update')")
    public Integer edit(@RequestBody Role role) throws Exception {
        roleService.updateRole(role);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("编辑角色" + JSON.toJSONString(role));
        return 1;
    }

    @RequestMapping(value = "/del")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(4, 'del')")
    public Integer del(@RequestBody List<Role> roles) throws Exception {
        roleService.delRole(roles);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("删除角色" + JSON.toJSONString(roles));
        return 1;
    }

    @RequestMapping(value = "/auth")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(4, 'auth')")
    public Integer auth(@RequestBody Auths auths) throws Exception {
        roleService.auth(auths);
        this.log("角色授权" + JSON.toJSONString(auths.getPermissions()));
        return 1;
    }

    @RequestMapping(value = "/getRoleAuths")
    @ResponseBody
    public List<RoleAuth> getRoleAuths(Long roleId) throws Exception {
        return roleService.getRoleAuths(roleId);
    }

    private void log(String text) throws Exception {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        log.info(user.getUsername() + text);
    }

}
