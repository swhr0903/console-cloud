package com.dxy.console.controller;

import com.alibaba.fastjson.JSON;
import com.dxy.console.po.Module;
import com.dxy.console.po.User;
import com.dxy.console.service.IndexService;
import com.dxy.console.service.ModuleService;
import com.dxy.console.service.Options;
import com.dxy.console.service.Paging;
import com.dxy.console.vo.TreeNode;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
@Controller
@RequestMapping(value = "/module")
@Slf4j(topic = "app")
public class ModuleController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ModuleService moduleService;
    @Autowired
    IndexService indexService;

    @RequestMapping(value = "/all")
    @PreAuthorize("authenticated and hasPermission(2, 'all')")
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
        model.addAttribute("page", "module.ftl");
        String avatar = user.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            model.addAttribute("avatar", avatar);
        }
        model.addAttribute("createTime", user.getCreate_time());
        model.addAttribute("menus", menus);
        model.addAttribute("title", "模块信息管理");
        model.addAttribute("summary", "系统模块数据");
        return "index";
    }

    @RequestMapping(value = "/isExist")
    @ResponseBody
    public Map<String, Object> isExist(@RequestBody Module params) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<com.dxy.console.vo.Module> module = moduleService.getModuleByName(params.getName());
        if (module != null) {
            result.put("code", 0);
            result.put("msg", "模块已存在");
        } else {
            result.put("code", 1);
            result.put("msg", "notExist");
        }
        return result;
    }

    @RequestMapping(value = "/getModulesPaging")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(2, 'query')")
    public Paging getModulesPaging(Integer limit, Integer offset, String name) throws Exception {
        return moduleService.getModules(limit, offset, name);
    }

    @RequestMapping(value = "/getModules")
    @ResponseBody
    public List<Module> getModules() throws Exception {
        Paging paging = moduleService.getModules(null, null, null);
        return paging.getRows();
    }

    @RequestMapping(value = "/add")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(2, 'add')")
    public Integer add(@RequestBody Module module) throws Exception {
        Long parentId = module.getParent_id();
        if (parentId != null && parentId > 0) {
            module.setIs_leaf(1);
        }
        moduleService.addModule(module);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("编辑模块" + JSON.toJSONString(module));
        return 1;
    }

    @RequestMapping(value = "/edit")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(2, 'update')")
    public Integer edit(@RequestBody Module module) throws Exception {
        Long parentId = module.getParent_id();
        if (parentId != null && parentId > 0) {
            module.setIs_leaf(1);
        }
        moduleService.updateModule(module);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("编辑模块" + JSON.toJSONString(module));
        return 1;
    }

    @RequestMapping(value = "/del")
    @ResponseBody
    @PreAuthorize("authenticated and hasPermission(2, 'del')")
    public Integer del(@RequestBody List<Module> modules) throws Exception {
        moduleService.delModule(modules);
        redisTemplate.opsForValue().set("menus", indexService.builderMenus());
        this.log("删除模块" + JSON.toJSONString(modules));
        return 1;
    }

    @RequestMapping(value = "/buildTree")
    @ResponseBody
    public List<TreeNode> buildTree() throws Exception {
        List<TreeNode> trees = new ArrayList<>();
        TreeNode tree = moduleService.buildTree();
        trees.add(tree);
        return trees;
    }

    @RequestMapping(value = "/getOptions")
    @ResponseBody
    public Map<String, Object> getOptions(String moduleName) throws Exception {
        Map<String, Object> oMap = new HashMap<>();
        List<com.dxy.console.vo.Module> modules;
        if (StringUtils.isNotBlank(moduleName)) {
            modules = moduleService.getModuleByName(moduleName);
            String[] options = modules.get(0).getOptions().split(",");
            StringBuilder stringBuilder = new StringBuilder();
            int i = 0;
            for (String option : options) {
                stringBuilder.append(Options.getCodeByName(option)).append(i < options.length - 1 ? "," : "");
                i++;
            }
            oMap.put("option", stringBuilder.toString());
        }
        List<Map<String, String>> options = new ArrayList<>();
        Map<String, String> map;
        for (Options option : Options.values()) {
            map = new HashMap<>();
            map.put("code", option.getCode());
            map.put("name", option.getName());
            options.add(map);
        }
        oMap.put("options", options);
        return oMap;
    }

    private void log(String text) throws Exception {
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        log.info(user.getUsername() + text);
    }

}
