package com.cloud.console;

import com.cloud.console.common.Constant;
import com.cloud.console.common.MutableHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by Frank on 2017/8/11.
 * JWT过滤器验证，验证除例外所有请求
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie c;
        MutableHttpServletRequest mutableHttpServletRequest = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                c = cookies[i];
                String cookieName = c.getName();
                if (cookieName.equals(Constant.TOKEN_HEADER_STRING)) {
                    mutableHttpServletRequest = new MutableHttpServletRequest(httpServletRequest);
                    String cookiValue = URLDecoder.decode(c.getValue(), "UTF-8");
                    mutableHttpServletRequest.putHeader(Constant.TOKEN_HEADER_STRING, cookiValue);
                }
            }
        }
        if (mutableHttpServletRequest != null) {
            Authentication authentication = TokenAuthenticationService.verifyToken(mutableHttpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(mutableHttpServletRequest, response);
        } else {
            filterChain.doFilter(httpServletRequest, response);
        }
    }
}
