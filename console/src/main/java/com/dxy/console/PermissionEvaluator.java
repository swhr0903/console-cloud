package com.dxy.console;

import com.dxy.console.po.User;
import com.dxy.console.service.RoleService;
import com.dxy.console.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 2017/9/4.
 * 权限校验
 */
@Component
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    @Autowired
    UserService userService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetId, Object permission){
        String userName = authentication.getName();
        if("admin".equals(userName)){
            return true;
        }
        com.dxy.console.vo.User params = new com.dxy.console.vo.User();
        params.setUsername(userName);
        User user = userService.getUser(params);
        Map<String, Object> authObj = new HashMap<>();
        authObj.put("userId", user.getId());
        authObj.put("moduleId", targetId != null ? (Integer) targetId : 0);
        authObj.put("permission", StringUtils.isNotBlank(permission.toString()) ? permission.toString() : "");
        return userService.authorized(authObj);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
