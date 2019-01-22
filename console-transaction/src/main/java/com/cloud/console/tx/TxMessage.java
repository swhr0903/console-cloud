package com.cloud.console.tx;

import lombok.Getter;
import lombok.Setter;

/** Created by Frank on 2019-01-18. */
@Getter
@Setter
public class TxMessage {
  private String txId;
  private Transaction transaction;
  private String txConsumer;
}
