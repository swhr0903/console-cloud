package com.dxy.console;

import com.dxy.console.common.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Collection;

/**
 * Created by Frank on 2017/8/7.
 * Spring Security自定义验证
 */
@Component
@Slf4j(topic = "login")
public class AuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {


    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        UserDetails user = userDetailsService.loadUserByUsername(username);
        if (user == null) {
            throw new BadCredentialsException("帐号不存在");
        }
        String encodePwd;
        try {
            String keyfile = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "keys/" + user.getUsername() + ".txt";
            //String keyfile = "keys/" + user.getUsername() + ".txt";
            encodePwd = RSAUtils.toHexString(RSAUtils.encrypt((RSAPublicKey)
                    RSAUtils.loadKey(keyfile, 1), password.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException("密码错误");
        }
        if (StringUtils.isBlank(encodePwd) || !encodePwd.equals(user.getPassword())) {
            throw new BadCredentialsException("密码错误");
        }
        log.info("用户" + user.getUsername() + "登录成功");
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    @Override
    public boolean supports(Class<?> arg0) {
        return true;
    }
}
