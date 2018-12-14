package com.cloud.console.service;

import com.cloud.console.common.MenuUtils;
import com.cloud.console.mapper.IndexMapper;
import com.cloud.console.mapper.ModuleMapper;
import com.cloud.console.po.Module;
import com.cloud.console.vo.TreeNode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/** Created by Frank on 2017/8/3. */
@Service
public class ModuleServiceImpl implements ModuleService {

  @Autowired ModuleMapper moduleMapper;
  @Autowired IndexMapper indexMapper;

  @Override
  public Paging getModules(Integer limit, Integer offset, String name) {
    List<com.cloud.console.vo.Module> modules = null;
    if (StringUtils.isNotBlank(name)) {
      modules = moduleMapper.getModuleByName(name);
    } else if (limit == null && offset == null && StringUtils.isBlank(name)) {
      modules = moduleMapper.getModules(null);
    } else {
      if (offset != null && limit != null) {
        Map<String, Integer> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        modules = moduleMapper.getModules(params);
      }
    }
    Paging paging = new Paging();
    paging.setTotal(moduleMapper.getModuleCount(name));
    paging.setRows(modules);
    return paging;
  }

  @Override
  public List<com.cloud.console.vo.Module> getModuleByName(String name) {
    return moduleMapper.getModuleByName(name);
  }

  @Override
  public void addModule(Module module) {
    moduleMapper.insertModule(module);
  }

  @Override
  public void updateModule(Module module) {
    moduleMapper.updateModule(module);
  }

  @Override
  public void delModule(List<Module> modules) {
    moduleMapper.delModule(modules);
  }

  @Override
  public TreeNode buildTree() {
    TreeNode root = new TreeNode();
    List<TreeNode> tree = new ArrayList<>();
    List<com.cloud.console.po.Module> modules = indexMapper.getModules();
    Map menuMap = MenuUtils.rebuildMenus(modules);
    Iterator it = menuMap.keySet().iterator();
    while (it.hasNext()) {
      Map menu = (Map) menuMap.get(it.next());
      TreeNode node = new TreeNode();
      if ((Long) menu.get("parentId") == 0) {
        node.setText(String.valueOf(menu.get("name")));
        node.setValue(String.valueOf(menu.get("id")));
        String options = String.valueOf(menu.get("options"));
        if (StringUtils.isNotBlank(options) && !"null".equals(options)) {
          String[] optionArray = options.split(",");
          List<TreeNode> treeNodes = new ArrayList<>();
          for (String option : optionArray) {
            TreeNode treeNode = new TreeNode();
            treeNode.setText(Options.getNameByCode(option));
            treeNode.setValue(menu.get("id") + "-" + option);
            treeNodes.add(treeNode);
          }
          node.setNodes(treeNodes);
        }
      }
      node = MenuUtils.buildChildMenuJson(menu, node);
      tree.add(node);
    }
    root.setValue("0");
    root.setText("所有模块");
    root.setNodes(tree);
    return root;
  }
}
