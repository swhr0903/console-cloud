package com.cloud.console.mq;

import lombok.Getter;
import lombok.Setter;

/** Created by Frank on 2019-01-14. */
@Getter
@Setter
public class MQProperties {
  private String host;
  private Integer port;
  private String userName;
  private String password;
  private String virtualHost;
}
