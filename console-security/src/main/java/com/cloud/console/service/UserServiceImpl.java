package com.cloud.console.service;

import com.cloud.console.common.RSAUtils;
import com.cloud.console.mapper.UserMapper;
import com.cloud.console.po.User;
import com.cloud.console.vo.UserRole;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
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
    public User getUser(com.cloud.console.vo.User user) {
        return userMapper.getUser(user);
    }

    @Override
    public List<UserRole> getUserRoles(String param) {
        return userMapper.getUserRoles(param);
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
