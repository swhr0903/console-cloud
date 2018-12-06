package com.cloud.console.service;

import com.cloud.console.common.MenuUtils;
import com.cloud.console.mapper.IndexMapper;
import com.cloud.console.po.Module;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    IndexMapper indexMapper;

    @Override
    public List<Module> getModules() throws Exception {
        return indexMapper.getModules();
    }

    @Override
    public String builderMenus() throws Exception {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("menus");
        List<Module> modules = this.getModules();
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
        StringBuilder stringBuilder = new StringBuilder(document.getRootElement().asXML());
        stringBuilder.delete(0, "<menus>".length());
        stringBuilder.delete(stringBuilder.length() - "</menus>".length(), stringBuilder.length());
        return stringBuilder.toString();
    }

    public List<Map<Integer, Integer>> getDWCount() throws Exception {
        return indexMapper.getDWCount();
    }

}
