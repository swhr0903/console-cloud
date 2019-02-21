package com.cloud.console.grpc;

import com.alibaba.fastjson.JSON;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

/** Created by Frank on 2019-02-15. */
@Service
public class RoleGrpcClient {

  @GrpcClient("RoleService")
  RoleServiceGrpc.RoleServiceBlockingStub roleServiceBlockingStub;

  public String getRoles() {
    RoleServiceProto.RoleList roleList =
        roleServiceBlockingStub.getRole(
            RoleServiceProto.Role.newBuilder().setName("frank").build());
    return JSON.toJSONString(roleList);
  }
}
