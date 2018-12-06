package com.cloud.console.service;

import com.cloud.console.po.Role;
import com.cloud.console.po.RoleAuth;
import com.cloud.console.vo.Auths;

import java.util.List;

/**
 * Created by Frank on 2017/8/3.
 */
public interface RoleService {

    /**
     * 获取角色
     *
     * @return
     */
    Paging getRoles(Integer limit, Integer offset, String code, String name) throws Exception;

    /**
     * 新增角色
     *
     * @param module
     * @throws Exception
     */
    void addRole(Role module) throws Exception;

    /**
     * 更新角色
     *
     * @param module
     * @throws Exception
     */
    void updateRole(Role module) throws Exception;

    /**
     * 删除角色
     *
     * @param modules
     */
    void delRole(List<Role> modules) throws Exception;

    /**
     * 角色授权
     *
     * @param auths
     */
    void auth(Auths auths) throws Exception;

    /**
     * 获得角色权限
     *
     * @param roleId
     * @return
     */
    List<RoleAuth> getRoleAuths(Long roleId) throws Exception;

}
