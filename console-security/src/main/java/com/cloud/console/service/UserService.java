package com.cloud.console.service;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(com.cloud.console.vo.User user);

    /**
     * 获取用户角色
     *
     * @param param
     * @return
     */
    List<UserRole> getUserRoles(String param);

    /**
     * 验证用户权限
     *
     * @param authObj
     * @return
     */
    Boolean authorized(Map<String, Object> authObj);

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUser(User user) throws Exception;

    /**
     * 7天内注册人数
     *
     * @return
     */
    List<Integer> regiUserCount7();

    /**
     * 7天内注册中充值人数
     *
     * @return
     */
    List<Integer> regiDepositCount7();

    /**
     * 7天内充值总额
     *
     * @return
     */
    List<Integer> depositSum7();

    /**
     * 7天内提现总额
     *
     * @return
     */
    List<Integer> withdrawSum7();
}
