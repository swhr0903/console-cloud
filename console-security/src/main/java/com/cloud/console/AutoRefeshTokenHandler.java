package com.cloud.console;

import com.cloud.console.common.Constant;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Created by Frank on 2018-12-10. */
public class AutoRefeshTokenHandler implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    Cookie[] cookies = request.getCookies();
    String token = "";
    for (int i = 0; cookies != null && i < cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (Constant.TOKEN_HEADER_STRING.equals(cookie.getName())) {
        token = cookie.getValue();
        break;
      }
    }
    if (StringUtils.isNotBlank(token)) {
      Cookie cookie = new Cookie(Constant.TOKEN_HEADER_STRING, token);
      cookie.setHttpOnly(true);
      cookie.setMaxAge(Constant.TOKEN_EXPIRATION_TIME);
      response.addCookie(cookie);
    }
    return true;
  }
}
