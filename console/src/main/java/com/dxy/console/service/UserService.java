package com.dxy.console.service;

import com.dxy.console.po.User;
import com.dxy.console.vo.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
public interface UserService {

    /**
     * 获取所有用户
     *
     * @return
     */
    Paging getUsers(Integer limit, Integer offset, String sortName, String sortOrder, String username, String startTime,
                    String endTime) throws Exception;

    /**
     * 获取用户总数
     *
     * @return
     */
    Integer getUserCount(String username, String startTime, String endTime) throws Exception;

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(com.dxy.console.vo.User user);

    /**
     * 获取用户角色
     *
     * @param param
     * @return
     */
    List<UserRole> getUserRoles(String param);


    /**
     * 插入用户信息
     *
     * @param user
     * @return
     * @throws Exception
     */
    void insertUser(User user) throws Exception;

    /**
     * 更新用户信息
     *
     * @param user
     */
    void updateUser(User user) throws Exception;

    /**
     * 删除用户信息
     *
     * @param user
     */
    void delUser(List<User> user) throws Exception;

    /**
     * 授予角色
     *
     * @param userId
     * @param roles
     * @return
     */
    void auth(Long userId, String roles) throws Exception;

    /**
     * 保存头像
     *
     * @param filePath
     * @param userName
     * @param file
     */
    void saveImg(String filePath, String userName, MultipartFile file) throws Exception;

    /**
     * 验证用户权限
     *
     * @param authObj
     * @return
     */
    Boolean authorized(Map<String, Object> authObj);

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
