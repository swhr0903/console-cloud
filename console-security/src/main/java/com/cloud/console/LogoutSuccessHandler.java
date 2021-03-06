package com.cloud.console;

import com.cloud.console.common.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** Created by Frank on 2017/8/16. 登出清除token */
@Component
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
  private static final Logger logger = LoggerFactory.getLogger("log");

  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    try {
      Cookie cookie = new Cookie(Constant.TOKEN_HEADER_STRING, null);
      cookie.setMaxAge(0);
      cookie.setPath("/");
      response.addCookie(cookie);
      response.sendRedirect("/login");
    } catch (Exception e) {
      logger.info("清空Cookies发生异常！" + e.getMessage());
    }
  }
}
