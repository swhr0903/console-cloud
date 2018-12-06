package com.cloud.console.service;

import com.cloud.console.po.Module;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
public interface IndexService {

    /**
     * 获取所有模块
     *
     * @return
     */
    List<Module> getModules() throws Exception;

    /**
     * 构造菜单
     *
     * @return
     */
    String builderMenus() throws Exception;

    /**
     * 获得未处理存款数
     *
     * @return
     */
    List<Map<Integer, Integer>> getDWCount() throws Exception;

}
