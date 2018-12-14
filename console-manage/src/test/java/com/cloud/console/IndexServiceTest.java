package com.cloud.console;

import com.alibaba.fastjson.JSON;
import com.cloud.console.common.MenuUtils;
import com.cloud.console.po.Module;
import com.cloud.console.service.IndexService;
import com.cloud.console.service.Paging;
import com.cloud.console.service.SecurityService;
import com.cloud.console.service.UserService;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {

  @Autowired IndexService indexService;
  @Autowired
  UserService userService;

  @Test
  public void testGetUsers() throws Exception {
    Paging users = userService.getUsers(2, 0, null, null, null, null, null);
    System.out.println(JSON.toJSONString(users));
  }

  @Test
  public void testGetModules() throws Exception {
    Document document = DocumentHelper.createDocument();
    Element root = document.addElement("menus");
    List<Module> modules = indexService.getModules();
    Map menuMap = MenuUtils.rebuildMenus(modules);
    Iterator it = menuMap.keySet().iterator();
    while (it.hasNext()) {
      Map menu = (Map) menuMap.get(it.next());
      Element liElement = root.addElement("li");
      if ((Long) menu.get("parentId") == 0) {
        liElement.addAttribute("class", "treeview");
        Element aElement = liElement.addElement("a");
        aElement.addAttribute("href", "#");
        Element iElement = aElement.addElement("i");
        iElement.addAttribute("class", "fa fa-share");
        iElement.addText("");
        Element sElement = aElement.addElement("span");
        sElement.addText(String.valueOf(menu.get("name")));
        Element spanElement = aElement.addElement("span");
        spanElement.addAttribute("class", "pull-right-container");
        Element iElement1 = spanElement.addElement("i");
        iElement1.addAttribute("class", "fa fa-angle-left pull-right");
        iElement1.setText("");
      }
      MenuUtils.buildChildMenuXml(menu, document, liElement);
    }
    System.out.println("菜单html----------" + document.asXML());
  }
}
