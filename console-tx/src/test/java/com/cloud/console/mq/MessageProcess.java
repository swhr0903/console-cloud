package com.cloud.console.mq;

import com.cloud.console.common.Response;
import org.apache.commons.lang.StringUtils;

/** Created by Frank on 2019-01-15. */
public class MessageProcess implements ResponseProcess<String> {
  @Override
  public Response process(String message) {
    Response response;
    if (StringUtils.isNotBlank(message)) {
      System.out.println("message:" + message);
      response = new Response(true, "");
    } else {
      response = new Response(false, "message is null");
    }
    return response;
  }
}
