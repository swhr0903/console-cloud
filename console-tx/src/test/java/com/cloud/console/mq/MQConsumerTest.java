package com.cloud.console.mq;

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
public class MQConsumerTest {

  @Autowired MQBuilder mqBuilder;

  @Test
  public void testConsume() throws IOException {
    Consumer consumer =
        mqBuilder.buildMessageConsumer(
            "testExchange", "test", "testQueue", new MessageProcess(), "direct");
    Response response = consumer.consume();
    Assert.assertNotNull(response);
  }
}
