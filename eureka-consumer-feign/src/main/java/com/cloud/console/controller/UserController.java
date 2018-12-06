package com.cloud.console.controller;

import com.cloud.console.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Frank on 2018/3/27.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserClient userClient;

    @GetMapping("/isExist/{userName}")
    public String isExist(@PathVariable String userName) {
        return userClient.isExist(userName);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces =
            {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public String register(@RequestBody User user) {
        return userClient.register(user);
    }
}
