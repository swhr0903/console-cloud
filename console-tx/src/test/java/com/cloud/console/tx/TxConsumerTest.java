package com.cloud.console.tx;

import com.cloud.console.App;
import com.cloud.console.common.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/** Created by Frank on 2019-01-16. */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class TxConsumerTest {

  @Autowired TxConsumer txConsumer;

  @Test
  public void consume() throws IOException {
    Response response = txConsumer.consume();
    Assert.assertEquals(response.getIsSuccess(), true);
  }
}
