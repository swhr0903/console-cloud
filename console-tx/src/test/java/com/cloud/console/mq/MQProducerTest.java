package com.cloud.console.mq;

import com.alibaba.fastjson.JSON;
import com.cloud.console.App;
import com.cloud.console.common.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/** Created by Frank on 2019-01-15. */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class MQProducerTest {

  @Autowired MQBuilder mqBuilder;

  @Test
  public void testProducer() throws IOException {
    Producer producer =
        mqBuilder.buildMessageProducer("testExchange", "test", "testQueue", "direct");
    Response response = producer.send("test");
    System.out.println(JSON.toJSONString(response));
    Assert.assertNotNull(response);
  }
}
