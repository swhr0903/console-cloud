package com.cloud.console;

import com.cloud.console.common.Constant;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Created by Frank on 2018-12-10. */
public class AutoRefeshTokenHandler implements HandlerInterceptor {

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    Cookie[] cookies = request.getCookies();
    for (int i = 0; cookies != null && i < cookies.length; i++) {
      Cookie cookie = cookies[i];
      if (Constant.TOKEN_HEADER_STRING.equals(cookie.getName())) {
        Cookie newCookie = new Cookie(Constant.TOKEN_HEADER_STRING, cookie.getValue());
        newCookie.setMaxAge(Constant.TOKEN_EXPIRATION_TIME);
        newCookie.setHttpOnly(true);
        response.addCookie(newCookie);
        break;
      }
    }
    return true;
  }
}
