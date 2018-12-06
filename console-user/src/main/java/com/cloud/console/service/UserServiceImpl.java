package com.cloud.console.service;

import com.cloud.console.mapper.UserMapper;
import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/3.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserMapper userMapper;

    @Override
    public Paging getUsers(Integer limit, Integer offset, String sortName, String sortOrder,
                           String username, String startTime, String endTime) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("offset", offset);
        params.put("limit", limit);
        params.put("sort", (StringUtils.isNotBlank(sortName) ? sortName : "id") + " " +
                (StringUtils.isNotBlank(sortOrder) ? sortOrder : ""));
        params.put("username", username);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        List<User> users = userMapper.getUsers(params);
        Paging paging = new Paging();
        paging.setTotal(userMapper.getUserCount(username, startTime, endTime));
        paging.setRows(users);
        return paging;
    }

    @Override
    public Integer getUserCount(String username, String startTime, String endTime) throws Exception {
        return userMapper.getUserCount(username, startTime, endTime);
    }

    @Override
    public User getUser(com.cloud.console.vo.User user) {
        return userMapper.getUser(user);
    }

    @Override
    public List<UserRole> getUserRoles(String param) {
        return userMapper.getUserRoles(param);
    }

    @Override
    @Transactional
    public void insertUser(User user) throws Exception {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userMapper.insertUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws Exception {
        String password = user.getPassword();
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        userMapper.updateUser(user);
    }

    @Override
    public void delUser(List<User> users) throws Exception {
        userMapper.delUser(users);
    }

    @Override
    @Transactional
    public void auth(Long userId, String roles) throws Exception {
        List<UserRole> userRoles = new ArrayList<>();
        UserRole userRole;
        for (String role : roles.split(",")) {
            userRole = new UserRole();
            userRole.setUser_id(userId);
            userRole.setRole_id(Long.parseLong(role));
            userRoles.add(userRole);
        }
        userRole = new UserRole();
        userRole.setUser_id(userId);
        userMapper.delUsereRole(userRole);
        userMapper.batchInsertUserRole(userRoles);
    }

    @Override
    @Transactional
    public void saveImg(String filePath, String userName, MultipartFile multipartFile) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        InputStream inputStream = multipartFile.getInputStream();
        File tmpFile = new File(userName + fileName.substring(fileName.lastIndexOf(".")));
        tmpFile.createNewFile();
        FileUtils.copyInputStreamToFile(inputStream, tmpFile);
        File fileDir = new File(filePath);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        FileUtils.copyFileToDirectory(tmpFile, fileDir);
        tmpFile.delete();
        ValueOperations<String, User> valueOperations = redisTemplate.opsForValue();
        User user = valueOperations.get(userName);
        user.setPassword(null);
        user.setAvatar("/static/avatars/" + userName + fileName.substring(fileName.lastIndexOf(".")));
        this.updateUser(user);
    }
}
