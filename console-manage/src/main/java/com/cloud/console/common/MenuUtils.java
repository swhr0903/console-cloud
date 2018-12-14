package com.cloud.console.common;

import com.cloud.console.po.Module;
import com.cloud.console.service.Options;
import com.cloud.console.vo.TreeNode;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.*;

/**
 * Created by Frank on 2017/8/4.
 *
 * <p>树形菜单处理类
 */
public class MenuUtils {

  /**
   * 重构菜单，构造成需要的结构
   *
   * @param modules
   * @return
   */
  public static Map rebuildMenus(List<Module> modules) {
    List<Map<String, Object>> moduleMap = new ArrayList<>();
    List<Map<String, Object>> roots = new ArrayList<>();
    Map<String, Object> menu;
    for (Module module : modules) {
      menu = new LinkedHashMap<>();
      menu.put("id", module.getId());
      menu.put("parentId", module.getParent_id());
      menu.put("name", module.getName());
      menu.put("url", module.getUrl());
      String options = module.getOptions();
      if (StringUtils.isNotBlank(options)) {
        menu.put("options", options);
      }
      moduleMap.add(menu);
      String parentId = String.valueOf(menu.get("parentId"));
      if ("0".equals(parentId)) {
        roots.add(menu);
      }
    }
    modules.removeAll(roots);
    HashMap menuMap = new LinkedHashMap();
    for (Map<String, Object> root : roots) {
      root = recursive(root, moduleMap);
      menuMap.put(root.get("id"), root);
    }
    return menuMap;
  }

  /**
   * 递归构造菜单Map
   *
   * @param menu
   * @param modules
   * @return
   */
  private static Map recursive(Map menu, List<Map<String, Object>> modules) {
    for (Map<String, Object> module : modules) {
      if (module.get("parentId").toString().equals(menu.get("id").toString())) {
        if (menu.get("children") == null) {
          menu.put("children", new ArrayList());
        }
        ((List) menu.get("children")).add(module);
        recursive(module, modules);
      }
    }
    return menu;
  }

  /**
   * 构造子菜单XML
   *
   * @param menu
   * @param document
   * @param element
   */
  public static Element buildChildMenuXml(Map menu, Document document, Element element) {
    List childList = (List) menu.get("children");
    if (childList != null) {
      Element ulElement = element.addElement("ul");
      ulElement.addAttribute("class", "treeview-menu");
      for (int i = 0; i < childList.size(); i++) {
        Map childMap = (Map) childList.get(i);
        Element liElement = ulElement.addElement("li");
        Element aElement = liElement.addElement("a");
        String url = String.valueOf(childMap.get("url"));
        aElement.addAttribute("href", StringUtils.isNotBlank(url) ? url : "#");
        Element iElement = aElement.addElement("i");
        iElement.addAttribute("class", "fa fa-circle-o");
        iElement.addText("");
        Element sElement = aElement.addElement("span");
        sElement.addText(String.valueOf(childMap.get("name")));
        if (childMap.get("children") != null) {
          Element spanElement = aElement.addElement("span");
          spanElement.addAttribute("class", "pull-right-container");
          Element iElement1 = spanElement.addElement("i");
          iElement1.addAttribute("class", "fa fa-angle-left pull-right");
          iElement1.setText("");
        }
        buildChildMenuXml(childMap, document, liElement);
      }
    }
    return element;
  }

  /**
   * 构造子菜单JSON
   *
   * @param menu
   */
  public static TreeNode buildChildMenuJson(Map menu, TreeNode node) {
    List childList = (List) menu.get("children");
    List<TreeNode> cList = new ArrayList<>();
    if (childList != null) {
      for (int i = 0; i < childList.size(); i++) {
        Map childMap = (Map) childList.get(i);
        TreeNode n = new TreeNode();
        n.setText(String.valueOf(childMap.get("name")));
        n.setValue(String.valueOf(childMap.get("id")));
        cList.add(n);
        buildChildMenuJson(childMap, n);
      }
    } else {
      String options = String.valueOf(menu.get("options"));
      if (StringUtils.isNotBlank(options) && !"null".equals(options)) {
        String[] optionArray = options.split(",");
        for (String option : optionArray) {
          TreeNode treeNode = new TreeNode();
          treeNode.setText(Options.getNameByCode(option));
          treeNode.setValue(menu.get("id") + "-" + option);
          cList.add(treeNode);
        }
      }
    }
    if (cList != null && cList.size() > 0) {
      node.setNodes(cList);
    }
    return node;
  }
}
