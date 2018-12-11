package com.cloud.console.service;

import com.cloud.console.mapper.RoleMapper;
import com.cloud.console.po.Role;
import com.cloud.console.po.RoleAuth;
import com.cloud.console.vo.Auths;
import com.cloud.console.vo.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2017/8/3. */
@Service
public class RoleServiceImpl implements RoleService {

  @Autowired RoleMapper roleMapper;

  @Override
  public Paging getRoles(Integer limit, Integer offset, String code, String name) throws Exception {
    Map<String, Object> params = new HashMap<>();
    params.put("code", code);
    params.put("name", name);
    Paging paging = new Paging();
    paging.setTotal(roleMapper.getRoleCount(params));
    params.put("offset", offset);
    params.put("limit", limit);
    List<Role> roles = roleMapper.getRoles(params);
    paging.setRows(roles);
    return paging;
  }

  @Override
  public void addRole(com.cloud.console.po.Role module) throws Exception {
    roleMapper.insertRole(module);
  }

  @Override
  public void updateRole(com.cloud.console.po.Role module) throws Exception {
    roleMapper.updateRole(module);
  }

  @Override
  public void delRole(List<com.cloud.console.po.Role> modules) throws Exception {
    roleMapper.delRole(modules);
  }

  @Override
  @Transactional
  public void auth(Auths auths) throws Exception {
    List<RoleAuth> roleAuths = new ArrayList<>();
    RoleAuth roleAuth;
    StringBuilder options;
    for (Permission permission : auths.getPermissions()) {
      roleAuth = new RoleAuth();
      options = new StringBuilder();
      roleAuth.setRole_id(auths.getRoleId());
      roleAuth.setModule_id(permission.getModuleId());
      int i = 0;
      List<String> optionList = permission.getOptions();
      for (String option : optionList) {
        options.append(option).append(i < optionList.size() - 1 ? "," : "");
        i++;
      }
      roleAuth.setPermission(options.toString());
      roleAuths.add(roleAuth);
    }
    RoleAuth params = new RoleAuth();
    params.setRole_id(roleAuths.get(0).getRole_id());
    roleMapper.delRoleAuth(params);
    roleMapper.batchInsertRoleAuth(roleAuths);
  }

  @Override
  public List<RoleAuth> getRoleAuths(Long roleId) throws Exception {
    return roleMapper.getRoleAuths(roleId);
  }
}
