package com.cloud.console.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.console.common.ExportUtils;
import com.cloud.console.po.Module;
import com.cloud.console.po.User;
import com.cloud.console.service.IndexService;
import com.cloud.console.service.ModuleService;
import com.cloud.console.service.Options;
import com.cloud.console.service.Paging;
import com.cloud.console.vo.TreeNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/** Created by Frank on 2017/8/3. */
@RestController
@RequestMapping("/module")
@Slf4j(topic = "app")
public class ModuleController {

  @Autowired RedisTemplate redisTemplate;
  @Autowired ModuleService moduleService;
  @Autowired IndexService indexService;

  @GetMapping(value = "/isExist")
  public Map<String, Object> isExist(Module params) throws Exception {
    Map<String, Object> result = new HashMap<>();
    List<com.cloud.console.vo.Module> modules = moduleService.getModuleByName(params.getName());
    if (CollectionUtils.isEmpty(modules)) {
      result.put("code", 1);
      result.put("msg", "notExist");
    } else {
      result.put("code", 0);
      result.put("msg", "模块已存在");
    }
    return result;
  }

  @GetMapping(value = "/getModulesPaging")
  @PreAuthorize("authenticated and hasPermission(2, 'query')")
  public Paging getModulesPaging(Integer limit, Integer offset, String name) throws Exception {
    return moduleService.getModules(limit, offset, name);
  }

  @GetMapping(value = "/getModules")
  @ResponseBody
  public List<Module> getModules() throws Exception {
    Paging paging = moduleService.getModules(null, null, null);
    return paging.getRows();
  }

  @PostMapping(value = "/add")
  @PreAuthorize("authenticated and hasPermission(2, 'add')")
  public Integer add(Module module) throws Exception {
    Long parentId = module.getParent_id();
    if (parentId != null && parentId > 0) {
      module.setIs_leaf(1);
    }
    moduleService.addModule(module);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("编辑模块" + JSON.toJSONString(module));
    return 1;
  }

  @PatchMapping(value = "/update")
  @PreAuthorize("authenticated and hasPermission(2, 'update')")
  public Integer edit(Module module) throws Exception {
    Long parentId = module.getParent_id();
    if (parentId != null && parentId > 0) {
      module.setIs_leaf(1);
    }
    moduleService.updateModule(module);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("编辑模块" + JSON.toJSONString(module));
    return 1;
  }

  @DeleteMapping(value = "/del")
  @PreAuthorize("authenticated and hasPermission(2, 'del')")
  public Integer del(@RequestBody List<Module> modules) throws Exception {
    moduleService.delModule(modules);
    redisTemplate.opsForValue().set("menus", indexService.builderMenus());
    this.log("删除模块" + JSON.toJSONString(modules));
    return 1;
  }

  @GetMapping(value = "/export")
  public Map<String, Object> export(@RequestParam LinkedHashMap<String, String> header)
      throws Exception {
    Map<String, Object> result = new HashMap<>();
    List<com.cloud.console.vo.Module> modules =
        moduleService.getModules(null, null, null).getRows();
    String fileUrl = ExportUtils.export("模块管理", header, modules);
    result.put("code", 1);
    result.put("msg", fileUrl);
    return result;
  }

  @GetMapping(value = "/buildTree")
  public List<TreeNode> buildTree() throws Exception {
    List<TreeNode> trees = new ArrayList<>();
    TreeNode tree = moduleService.buildTree();
    trees.add(tree);
    return trees;
  }

  @GetMapping(value = "/getOptions")
  public Map<String, Object> getOptions(String moduleName) throws Exception {
    Map<String, Object> oMap = new HashMap<>();
    List<com.cloud.console.vo.Module> modules;
    if (StringUtils.isNotBlank(moduleName)) {
      modules = moduleService.getModuleByName(moduleName);
      if (modules.size() > 0) {
        String[] options = modules.get(0).getOptions().split(",");
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (String option : options) {
          stringBuilder
              .append(Options.getCodeByName(option))
              .append(i < options.length - 1 ? "," : "");
          i++;
        }
        oMap.put("option", stringBuilder.toString());
      }
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
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    log.info(user.getUsername() + text);
  }
}
