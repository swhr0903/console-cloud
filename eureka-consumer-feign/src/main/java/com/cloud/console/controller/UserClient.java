package com.cloud.console.controller;

import com.cloud.console.po.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/** Created by Frank on 2018/3/28. */
@FeignClient(
    value = "console-manage",
    path = "user" /*, configuration = FeignFormSupportConfig.class*/)
public interface UserClient {

  @GetMapping("/isExist/{userName}")
  String isExist(@PathVariable String userName);

  @PostMapping(
      value = "/register",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
  String register(@RequestBody User user);
}
