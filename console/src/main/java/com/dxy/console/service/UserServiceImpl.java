package com.dxy.console.service;

import com.dxy.console.common.RSAUtils;
import com.dxy.console.mapper.UserMapper;
import com.dxy.console.po.Role;
import com.dxy.console.po.RoleAuth;
import com.dxy.console.po.User;
import com.dxy.console.vo.UserRole;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

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
    public User getUser(com.dxy.console.vo.User user) {
        return userMapper.getUser(user);
    }

    @Override
    public List<UserRole> getUserRoles(String param) {
        return userMapper.getUserRoles(param);
    }

    @Override
    @Transactional
    public void insertUser(User user) throws Exception {
        KeyPair keyPair = RSAUtils.generateKey();
        String keyfile = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                + "keys/" + user.getUsername() + ".txt";
        RSAUtils.saveKey(keyPair, keyfile);
        Key publicKey = RSAUtils.loadKey(keyfile, 1);
        byte[] e = RSAUtils.encrypt((RSAPublicKey) publicKey, user.getPassword().getBytes());
        user.setPassword(RSAUtils.toHexString(e));
        userMapper.insertUser(user);
    }

    @Override
    @Transactional
    public void updateUser(User user) throws Exception {
        String password = user.getPassword();
        if (StringUtils.isNotBlank(password)) {
            KeyPair keyPair = RSAUtils.generateKey();
            String keyfile = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "keys/" + user.getUsername() + ".txt";
            RSAUtils.saveKey(keyPair, keyfile);
            Key publicKey = RSAUtils.loadKey(keyfile, 1);
            byte[] e = RSAUtils.encrypt((RSAPublicKey) publicKey, user.getPassword().getBytes());
            user.setPassword(RSAUtils.toHexString(e));
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

    @Override
    public Boolean authorized(Map<String, Object> authObj) {
        Integer roleAuths = userMapper.getUserAuths(authObj);
        if (roleAuths > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Integer> regiUserCount7() {
        List<Integer> data = noDataFillZero(userMapper.regiUserCount7());
        return data;
    }

    @Override
    public List<Integer> regiDepositCount7() {
        List<Integer> data = noDataFillZero(userMapper.regiDepositCount7());
        return data;
    }

    @Override
    public List<Integer> depositSum7() {
        List<Integer> data = noDataFillZero(userMapper.depositSum7());
        return data;
    }

    @Override
    public List<Integer> withdrawSum7() {
        List<Integer> data = noDataFillZero(userMapper.withdrawSum7());
        return data;
    }

    private List<Integer> noDataFillZero(List<Map<String, Integer>> datas) {
        List<Integer> ls = new ArrayList<>();
        String beforeToday;
        for (int i = -6; i < 1; i++) {
            beforeToday = DateFormatUtils.format(DateUtils.addDays(new Date(), i), "yyyy-MM-dd");
            int j = 0;
            if (datas != null && datas.size() > 0) {
                a:
                for (Map<String, Integer> data : datas) {
                    for (Map.Entry<String, Integer> entity : data.entrySet()) {
                        if ("date".equals(entity.getKey())) {
                            if (beforeToday.equals(entity.getValue())) {
                                ls.add(data.get("num"));
                                break a;
                            } else {
                                if (j == datas.size() - 1) {
                                    ls.add(0);
                                }
                            }
                        }
                    }
                    j++;
                }
            } else {
                ls.add(0);
            }
        }
        return ls;
    }
}
