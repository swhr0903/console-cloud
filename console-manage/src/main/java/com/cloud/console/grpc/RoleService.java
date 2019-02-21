package com.cloud.console.grpc;

import com.cloud.console.mapper.RoleMapper;
import com.cloud.console.po.Role;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Created by Frank on 2019-02-15. */
@GrpcService
public class RoleService extends RoleServiceGrpc.RoleServiceImplBase {

  @Autowired RoleMapper roleMapper;

  @Override
  public void getRole(
      RoleServiceProto.Role request, StreamObserver<RoleServiceProto.RoleList> responseObserver) {
    Map<String, Object> params = new HashMap<>();
    if (StringUtils.isNotBlank(request.getCode())) {
      params.put("code", request.getCode());
    }
    if (StringUtils.isNotBlank(request.getName())) {
      params.put("name", request.getName());
    }
    List<Role> roles = roleMapper.getRoles(params);
    List list = new ArrayList();
    for (Role role : roles) {
      list.add(
          RoleServiceProto.Role.newBuilder()
              .setId(role.getId())
              .setCode(role.getCode())
              .setName(role.getName())
              .setStatus(role.getStatus())
              .build());
    }
    RoleServiceProto.RoleList roleList =
        RoleServiceProto.RoleList.newBuilder().addAllRole(list).build();
    responseObserver.onNext(roleList);
    responseObserver.onCompleted();
  }
}
