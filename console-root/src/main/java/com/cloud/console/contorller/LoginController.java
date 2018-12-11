package com.cloud.console.contorller;

import com.cloud.console.common.Constant;
import com.cloud.console.common.GoogleAuthenticator;
import com.cloud.console.common.MD5Utils;
import com.cloud.console.po.User;
import com.cloud.console.service.SecurityService;
import com.cloud.console.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/** Created by Frank on 2018-12-11. */
@Controller
public class LoginController {

  @Autowired RedisTemplate redisTemplate;
  @Autowired
  UserService userService;
  @Autowired JavaMailSender javaMailSender;

  @Value("${spring.mail.username}")
  private String username;

  @RequestMapping(value = "/login")
  public String login() {
    return "login";
  }

  @RequestMapping(value = "/secondAuth")
  private String secondAuth() {
    String userName =
        String.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    redisTemplate.opsForValue().set(userName + "-secondAuth", "0", 120, TimeUnit.SECONDS);
    return "secondAuth";
  }

  @RequestMapping(value = "/returnLogin")
  public String returnLogin(HttpServletResponse response) {
    try {
      Cookie cookie = new Cookie(Constant.TOKEN_HEADER_STRING, null);
      cookie.setMaxAge(0);
      cookie.setPath("/");
      response.addCookie(cookie);
      response.sendRedirect("/login");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "login";
  }

  @RequestMapping(value = "/forgetPwd")
  public String forgetPwd() {
    return "forgetPwd";
  }

  @RequestMapping(value = "/")
  public String index(Model model) {
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String userSecondAuth = (String) redisTemplate.opsForValue().get(userName + "-secondAuth");
    if (StringUtils.isBlank(userName)
        || StringUtils.isBlank(userSecondAuth)
        || "0".equals(userSecondAuth)) {
      return "/login";
    }
    ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
    String menus = StringOperations.get("menus");
    model.addAttribute("user", username);
    List<GrantedAuthority> grantedAuthorities =
        (List<GrantedAuthority>)
            SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    StringBuilder roles = new StringBuilder();
    for (int i = 0; i < grantedAuthorities.size(); i++) {
      GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
      roles
          .append(grantedAuthority.getAuthority().split("_")[1])
          .append(i < grantedAuthorities.size() - 1 ? "," : "");
    }
    if ("admin".equals(userName)) {
      model.addAttribute("role", "ADMIN");
    } else {
      model.addAttribute(
          "role", StringUtils.isNotBlank(roles.toString()) ? roles.toString() : "NEWBIE");
    }
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    model.addAttribute("page", "welcome.ftl");
    String avatar = user.getAvatar();
    if (StringUtils.isNotBlank(avatar)) {
      model.addAttribute("avatar", avatar);
    }
    model.addAttribute("createTime", user.getCreate_time());
    model.addAttribute("menus", menus);
    model.addAttribute("title", "欢迎");
    model.addAttribute("summary", "生活从这里开始");
    return "index";
  }

  @RequestMapping(value = "/verifyBackPwd")
  public String verifyBackPwd(Model model, String userName, String sid) {
    String rSid = (String) redisTemplate.opsForValue().get(userName + "-sid");
    if (rSid.equals(sid)) {
      model.addAttribute("userName", userName);
      return "restPwd";
    }
    return "";
  }

  @RequestMapping(value = "/getAuthConfig")
  public @ResponseBody Map<String, String> getAuthConfig(
      HttpServletRequest request, String username, String password) throws Exception {
    Map<String, String> result = new HashMap<>();
    com.cloud.console.vo.User params = new com.cloud.console.vo.User();
    params.setUsername(username);
    params.setStatus(1);
    User user = userService.getUser(params);
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
      result.put("code", "0");
      result.put("msg", "帐号或密码错误！");
    } else if (StringUtils.isNotBlank(user.getMfa_secret())) {
      result.put("code", "0");
      result.put("msg", "该帐号已绑定一台设备，如需修改请联系管理员！");
    } else {
      String secret = GoogleAuthenticator.generateSecretKey();
      User upateParams = new User();
      upateParams.setUsername(username);
      upateParams.setMfa_secret(secret);
      userService.updateUser(upateParams);
      String url = GoogleAuthenticator.getQRBarcodeURL(username, request.getServerName(), secret);
      result.put("code", "1");
      result.put("msg", url);
    }
    return result;
  }

  @RequestMapping(value = "/authGoogleCode")
  public @ResponseBody Map<String, String> authGoogleCode(
      Long authCode, HttpServletResponse response) {
    Map<String, String> result = new HashMap<>();
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    long t = System.currentTimeMillis();
    GoogleAuthenticator ga = new GoogleAuthenticator();
    ga.setWindowSize(5);
    boolean isAuth = ga.check_code(user.getMfa_secret(), authCode, t);
    if (isAuth) {
      redisTemplate.opsForValue().set(userName + "-secondAuth", "1", 36000, TimeUnit.SECONDS);
      result.put("1", "认证成功！");
    } else {
      result.put("0", "验证码错误！");
    }
    return result;
  }

  @RequestMapping(value = "/getBackPwd")
  public @ResponseBody Integer getBackPwd(HttpServletRequest request, String userName) {
    com.cloud.console.vo.User params = new com.cloud.console.vo.User();
    Map<String, String> result = new HashMap<>();
    params.setUsername(userName);
    User user = userService.getUser(params);
    if (user == null) {
      return 0;
    }
    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(userName);
    simpleMailMessage.setTo(user.getEmail());
    simpleMailMessage.setSubject("主题：DXY找回密码");
    StringBuffer requestURL = request.getRequestURL();
    String domainName =
        requestURL
            .delete(requestURL.length() - request.getRequestURI().length(), requestURL.length())
            .append("/")
            .toString();
    int num = (int) (Math.random() * 6);
    String sid = MD5Utils.generate(String.valueOf(num));
    redisTemplate.opsForValue().set(userName + "-sid", sid, 60, TimeUnit.SECONDS);
    String url = domainName + "verifyBackPwd?userName=" + userName + "&sid=" + sid;
    String text = "请点击下面链接重置密码\t\n" + url;
    simpleMailMessage.setText(text);
    javaMailSender.send(simpleMailMessage);
    result.put("1", "认证成功！");
    return 1;
  }

  @RequestMapping(value = "/restPwd")
  public @ResponseBody Integer restPwd(String username, String password) throws Exception {
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    userService.updateUser(user);
    return 1;
  }

  @RequestMapping(value = "/403")
  public String accessDenied(Model model) throws Exception {
    ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
    String menus = StringOperations.get("menus");
    String userName =
        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    if (StringUtils.isBlank(userName)) {
      return "login";
    }
    model.addAttribute("user", userName);
    List<GrantedAuthority> grantedAuthorities =
        (List<GrantedAuthority>)
            SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    StringBuilder roles = new StringBuilder();
    for (int i = 0; i < grantedAuthorities.size(); i++) {
      GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
      roles
          .append(grantedAuthority.getAuthority().split("_")[1])
          .append(i < grantedAuthorities.size() - 1 ? "," : "");
    }
    if (StringUtils.isNotBlank(roles.toString())) {
      model.addAttribute("role", roles.toString());
    } else {
      model.addAttribute("role", "NEWBIE");
    }
    ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
    User user = userOperations.get(userName);
    model.addAttribute("avatar", user.getAvatar());
    model.addAttribute("createTime", user.getCreate_time());
    model.addAttribute("menus", menus);
    return "403";
  }
}
