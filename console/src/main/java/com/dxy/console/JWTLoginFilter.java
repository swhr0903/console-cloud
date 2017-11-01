package com.dxy.console;

import com.alibaba.fastjson.JSON;
import com.dxy.console.common.Constant;
import com.dxy.console.vo.UserRole;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Frank on 2017/8/11.
 * 拦截auth请求，生成JWT token并写入response
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {

    private static final int EXPIRATION_TIME = 36000;

    public JWTLoginFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {
        String loginName = req.getParameter("username");
        String password = req.getParameter("password");
        if (StringUtils.isBlank(loginName) || StringUtils.isBlank(password)) {
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("status", 401);
            tokens.put("message", "没有登录名或密码");
            tokens.put("result", null);
            String json = JSON.toJSONString(tokens);
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json;charset=UTF-8");
            res.setStatus(HttpServletResponse.SC_OK);
            res.getOutputStream()
                    .write(json.getBytes("UTF-8"));
            return null;
        }
        return getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(loginName, password));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        StringBuilder commaBuilder = new StringBuilder();
        List<UserRole> roles = userDetails.getRoles();
        for (UserRole role : roles) {
            commaBuilder.append(role.getRole_code()).append(",");
        }
        String authorities = roles != null && roles.size() > 0 ? commaBuilder.substring(0, commaBuilder.length() - 1) : "";
        String token = TokenAuthenticationService.generateToken(userDetails.getUsername(), authorities);
        Cookie cookie = new Cookie(Constant.TOKEN_HEADER_STRING, token);
        cookie.setHttpOnly(true);
        //cookie.setSecure(true);
        cookie.setMaxAge(EXPIRATION_TIME);
        response.addCookie(cookie);
        //response.addHeader(Constant.TOKEN_HEADER_STRING, token);
    }
}
