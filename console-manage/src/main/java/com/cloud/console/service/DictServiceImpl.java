package com.cloud.console.service;

import com.cloud.console.mapper.DictMapper;
import com.cloud.console.po.Dict;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@Service
public class DictServiceImpl implements DictService {

  @Autowired DictMapper dictMapper;

  @Override
  public Paging getDicts(Integer limit, Integer offset, String name) {
    List<Dict> modules = null;
    if (StringUtils.isNotBlank(name)) {
      modules = dictMapper.getDictByName(name);
    } else if (limit == null && offset == null && StringUtils.isBlank(name)) {
      modules = dictMapper.getDicts(null);
    } else {
      if (offset != null && limit != null) {
        Map<String, Integer> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        modules = dictMapper.getDicts(params);
      }
    }
    Paging paging = new Paging();
    paging.setTotal(dictMapper.getDictCount(name));
    paging.setRows(modules);
    return paging;
  }

  @Override
  public List<Dict> getDictByName(String name) {
    return dictMapper.getDictByName(name);
  }

  @Override
  public void addDict(Dict dict) {
    dictMapper.insertDict(dict);
  }

  @Override
  public void updateDict(Dict dict) {
    dictMapper.updateDict(dict);
  }

  @Override
  public void delDict(List<Dict> dicts) {
    dictMapper.delDict(dicts);
  }
}
