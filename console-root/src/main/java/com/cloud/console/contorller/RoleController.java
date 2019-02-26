package com.cloud.console.contorller;

import com.cloud.console.grpc.RoleGrpcClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Created by Frank on 2019-02-18. */
@RestController
@RequestMapping("/role")
public class RoleController {
  @Autowired RoleGrpcClient roleGrpcClient;

  @GetMapping("/")
  public String getRoles() {
    String roles = roleGrpcClient.getRoles();
    //System.out.println(roles);
    return roles;
  }
}
