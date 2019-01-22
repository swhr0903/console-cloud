package com.cloud.console.mq;

import com.cloud.console.common.MessageWithTime;
import com.cloud.console.common.Response;

/** Created by Frank on 2019-01-14. */
public interface Producer {

  Response send(Object message);

  Response send(MessageWithTime messageWithTime);
}
