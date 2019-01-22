package com.cloud.console.tx;

import com.cloud.console.common.Response;
import com.cloud.console.mq.MQBuilder;
import com.cloud.console.mq.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/** Created by Frank on 2019-01-15. */
@Component
public class TxPoducer {

  @Autowired MQBuilder mqBuilder;

  public Boolean process(TxMessage txMessage) throws IOException {
    Producer producer = mqBuilder.buildMessageProducer("txExchange", "tx", "txQueue", "direct");
    Response response = producer.send(txMessage);
    return response.getIsSuccess();
  }
}
