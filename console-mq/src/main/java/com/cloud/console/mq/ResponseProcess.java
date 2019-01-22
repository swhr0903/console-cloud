package com.cloud.console.mq;

import com.cloud.console.common.Response;

/** Created by Frank on 2019-01-15. */
public interface ResponseProcess<T> {
  Response process(T message);
}
