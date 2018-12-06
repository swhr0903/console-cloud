package com.cloud.console.service;

import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
    User getUser(com.cloud.console.vo.User user);

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
}
