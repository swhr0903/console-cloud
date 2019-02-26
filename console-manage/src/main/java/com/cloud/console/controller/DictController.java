package com.cloud.console.controller;

import com.alibaba.fastjson.JSON;
import com.cloud.console.Result;
import com.cloud.console.common.Logger;
import com.cloud.console.po.Dict;
import com.cloud.console.po.Module;
import com.cloud.console.service.DictService;
import com.cloud.console.service.Paging;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Created by Frank on 2018-12-21. */
@RestController
@RequestMapping("/dict")
@Api
public class DictController {

  @Autowired RedisTemplate redisTemplate;
  @Autowired DictService dictService;
  @Autowired
  Logger logger;

  @GetMapping(value = "/isExist")
  public Result isExist(Dict params) throws Exception {
    Result result = new Result();
    List<Dict> modules = dictService.getDictByName(params.getDict_name());
    if (CollectionUtils.isEmpty(modules)) {
      result.setCode("1");
      result.setMsg("notExist");
    } else {
      result.setCode("0");
      result.setMsg("字段已存在");
    }
    return result;
  }

  @GetMapping(value = "/getDictsPaging")
  @PreAuthorize("authenticated and hasPermission(2, 'query')")
  public Paging getDictPaging(Integer limit, Integer offset, String name) throws Exception {
    return dictService.getDicts(limit, offset, name);
  }

  @GetMapping(value = "/getDicts")
  @ResponseBody
  public List<Module> getDicts() throws Exception {
    Paging paging = dictService.getDicts(null, null, null);
    return paging.getRows();
  }

  @PatchMapping(value = "/update")
  @PreAuthorize("authenticated and hasPermission(2, 'update')")
  @ApiOperation("系统字段修改")
  public Result edit(Dict dict) throws Exception {
    Result result = new Result();
    dictService.updateDict(dict);
    logger.log("修改系统字段" + JSON.toJSONString(dict));
    result.setCode("1");
    result.setMsg("修改成功");
    return result;
  }

  @DeleteMapping(value = "/del")
  @PreAuthorize("authenticated and hasPermission(2, 'del')")
  public Result del(@RequestBody List<Dict> modules) throws Exception {
    Result result = new Result();
    dictService.delDict(modules);
    logger.log("删除系统字段" + JSON.toJSONString(modules));
    result.setCode("1");
    result.setCode("删除成功");
    return result;
  }
}
