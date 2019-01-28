package com.cloud.console.tx;

import com.cloud.console.common.Response;
import com.cloud.console.mq.Consumer;
import com.cloud.console.mq.MQBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Created by Frank on 2019-01-15. */
@Component
public class TxConsumer {

  @Autowired TxPoducer txPoducer;

  @Autowired MQBuilder mqBuilder;

  @Autowired TxMessageProcess txMessageProcess;

  public Response consume() throws IOException {
    Consumer consumer =
        mqBuilder.buildMessageConsumer("txExchange", "tx", "txQueue", txMessageProcess, "direct");
    Response response = consumer.consume();
    return response;
  }
}
