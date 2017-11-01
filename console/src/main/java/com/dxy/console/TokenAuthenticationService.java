package com.dxy.console;

import com.dxy.console.common.Constant;
import com.dxy.console.service.UserService;
import com.dxy.console.vo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * Created by Frank on 2017/8/11.
 * JWT服务类（生成、验证）
 */
@Component
public class TokenAuthenticationService {

    private static final long EXPIRATION_TIME = 36000000;
    private static final String SECRET = "l4C@v9#I";
    private static final String TOKEN_PREFIX = "Bearer";

    @Autowired
    UserService uService;

    private static UserService userService;

    @PostConstruct
    public void initialize() {
        userService = uService;
    }

    /**
     * JWT生成
     *
     * @param username
     * @param authorities
     */
    protected static String generateToken(String username, String authorities) {
        String JWT = Jwts.builder()
                // 保存权限（角色）
                .claim("authorities", authorities)
                // 用户名写入标题
                .setSubject(username)
                // 有效期设置
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                // 签名设置
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        String token = "";
        try {
            token = URLEncoder.encode(TOKEN_PREFIX + (char) 32 + JWT, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * JWT验证
     *
     * @param request
     * @return
     */
    protected static Authentication verifyToken(HttpServletRequest request) {
        String token = request.getHeader(Constant.TOKEN_HEADER_STRING);
        if (token != null) {
            // 解析 Token
            Claims claims = Jwts.parser()
                    // 验签
                    .setSigningKey(SECRET)
                    // 去掉 Bearer
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody();
            // 用户名
            String userName = claims.getSubject();
            //验证用户是否可用
            User params = new User();
            params.setUsername(userName);
            params.setStatus(0);
            com.dxy.console.po.User user = userService.getUser(params);
            if (user != null) {
                return null;
            }
            // 权限（角色）
            List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(
                    (String) claims.get("authorities"));
            // 返回验证令牌
            return userName != null ?
                    new UsernamePasswordAuthenticationToken(userName, null, authorities) :
                    null;
        }
        return null;
    }
}
