package com.dxy.console.controller;

import com.alibaba.fastjson.JSON;
import com.dxy.console.common.Constant;
import com.dxy.console.common.GoogleAuthenticator;
import com.dxy.console.common.MD5Utils;
import com.dxy.console.common.RSAUtils;
import com.dxy.console.po.User;
import com.dxy.console.service.IndexService;
import com.dxy.console.service.UserService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Frank on 2017/8/3.
 */
@Controller
public class IndexController {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    IndexService indexService;
    @Autowired
    UserService userService;

    @Value("${spring.mail.username}")
    private String username;

    @RequestMapping(value = "/")
    public String index(Model model) throws Exception {
        ValueOperations<String, String> StringOperations = redisTemplate.opsForValue();
        String menus = StringOperations.get("menus");
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(userName)) {
            return "login";
        }
        model.addAttribute("user", userName);
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        StringBuilder roles = new StringBuilder();
        for (int i = 0; i < grantedAuthorities.size(); i++) {
            GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
            roles.append(grantedAuthority.getAuthority().split("_")[1])
                    .append(i < grantedAuthorities.size() - 1 ? "," : "");
        }
        if ("admin".equals(userName)) {
            model.addAttribute("role", "ADMIN");
        } else {
            model.addAttribute("role", StringUtils.isNotBlank(roles.toString()) ? roles.toString() : "NEWBIE");
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

    @RequestMapping(value = "/login")
    public String login() throws Exception {
        return "login";
    }

    @RequestMapping(value = "/getAuthConfig")
    public @ResponseBody
    Map<String, String> getAuthConfig(String username, String password) throws Exception {
        Map<String, String> result = new HashMap<>();
        com.dxy.console.vo.User params = new com.dxy.console.vo.User();
        params.setUsername(username);
        String encodePwd = "";
        try {
            String keyfile = Thread.currentThread().getContextClassLoader().getResource("").getPath()
                    + "keys/" + username + ".txt";
            encodePwd = RSAUtils.toHexString(RSAUtils.encrypt((RSAPublicKey)
                    RSAUtils.loadKey(keyfile, 1), password.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.setPassword(encodePwd);
        User user = userService.getUser(params);
        if (user == null) {
            result.put("code", "0");
            result.put("msg", "帐号或密码错误！");
        } else if (StringUtils.isNotBlank(user.getMfa_secret())) {
            result.put("code", "0");
            result.put("msg", "改帐号已绑定一台设备，如需修改请联系管理员！");
        } else {
            String secret = GoogleAuthenticator.generateSecretKey();
            User upateParams = new User();
            upateParams.setUsername(username);
            upateParams.setMfa_secret(secret);
            userService.updateUser(upateParams);
            String url = GoogleAuthenticator.getQRBarcodeURL(username, "localhost", secret);
            result.put("code", "1");
            result.put("msg", url);
        }
        return result;
    }

    @RequestMapping(value = "/secondAuth")
    private String secondAuth() {
        return "secondAuth";
    }

    @RequestMapping(value = "/authGoogleCode")
    @ResponseBody
    private Map<String, String> authGoogleCode(Long authCode, HttpServletResponse response) throws Exception {
        Map<String, String> result = new HashMap<>();
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ValueOperations<String, User> userOperations = redisTemplate.opsForValue();
        User user = userOperations.get(userName);
        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(5);
        boolean isAuth = ga.check_code(user.getMfa_secret(), authCode, t);
        if (isAuth) {
            result.put("1", "认证成功！");
        } else {
            try {
                Cookie cookie = new Cookie(Constant.HEADER_STRING, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
                response.sendRedirect("/login");
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("1", "验证码错误！");
        }
        return result;
    }

    @RequestMapping(value = "/forgetPwd")
    public String forgetPwd() throws Exception {
        return "forgetPwd";
    }

    @RequestMapping(value = "/getBackPwd")
    @ResponseBody
    public Integer getBackPwd(HttpServletRequest request, String userName) throws Exception {
        com.dxy.console.vo.User params = new com.dxy.console.vo.User();
        params.setUsername(userName);
        User user = userService.getUser(params);
        if (user == null) {
            return 0;
        }
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("主题：DXY找回密码");
        StringBuffer requestURL = request.getRequestURL();
        String domainName = requestURL.delete(requestURL.length() - request.getRequestURI().length(),
                requestURL.length()).append("/").toString();
        int num = (int) (Math.random() * 6);
        String sid = MD5Utils.generate(String.valueOf(num));
        redisTemplate.opsForValue().set(userName + "-sid", sid, 60, TimeUnit.SECONDS);
        String url = domainName + "verifyBackPwd?userName=" + userName + "&sid=" + sid;
        String text = "请点击下面链接重置密码\t\n" + url;
        simpleMailMessage.setText(text);
        javaMailSender.send(simpleMailMessage);
        return 1;
    }

    @RequestMapping(value = "/verifyBackPwd")
    public String verifyBackPwd(Model model, String userName, String sid) throws Exception {
        String rSid = (String) redisTemplate.opsForValue().get(userName + "-sid");
        if (rSid.equals(sid)) {
            model.addAttribute("userName", userName);
            return "restPwd";
        }
        return "";
    }

    @RequestMapping(value = "/restPwd")
    @ResponseBody
    public Integer restPwd(String username, String password) throws Exception {
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
        String userName = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (StringUtils.isBlank(userName)) {
            return "login";
        }
        model.addAttribute("user", userName);
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>) SecurityContextHolder.getContext()
                .getAuthentication().getAuthorities();
        StringBuilder roles = new StringBuilder();
        for (int i = 0; i < grantedAuthorities.size(); i++) {
            GrantedAuthority grantedAuthority = grantedAuthorities.get(i);
            roles.append(grantedAuthority.getAuthority().split("_")[1])
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

    @RequestMapping(value = "/getDWCount")
    public @ResponseBody
    List<Map<Integer, Integer>> getDWCount() throws Exception {
        return indexService.getDWCount();
    }

    @RequestMapping(value = "/regiUserCount7")
    public @ResponseBody
    Map<String, List<Integer>> regiUserCount7() {
        Map<String, List<Integer>> stats = new HashMap();
        stats.put("regiUserCount7", userService.regiUserCount7());
        stats.put("regiDepositCount7", userService.regiDepositCount7());
        return stats;
    }

    @RequestMapping(value = "/depositWCount7")
    public @ResponseBody
    Map<String, List<Integer>> depositWCount7() {
        Map<String, List<Integer>> stats = new HashMap();
        stats.put("depositSum7", userService.depositSum7());
        stats.put("withdrawSum7", userService.withdrawSum7());
        return stats;
    }
}
