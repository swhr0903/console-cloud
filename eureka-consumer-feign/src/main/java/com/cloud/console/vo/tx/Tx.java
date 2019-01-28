package com.cloud.console.vo.tx;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/** Created by Frank on 2019-01-16. */
@Getter
@Setter
public class Tx {
  private String txId;
  private List<Transaction> transactions;
  private Integer status;
}
