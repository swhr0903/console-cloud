package com.cloud.console.grpc;

import com.cloud.console.App;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/** Created by Frank on 2019-02-15. */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class RoleGrpcClientTest {

  @Autowired RoleGrpcClient roleGrpcClient;

  @Test
  public void getRoles() {
    String roles = roleGrpcClient.getRoles();
    Assert.assertNotNull(roles);
  }
}
