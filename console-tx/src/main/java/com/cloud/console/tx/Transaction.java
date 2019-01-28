package com.cloud.console.tx;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/** Created by Frank on 2019-01-16. */
@Getter
@Setter
public class Transaction implements Cloneable {
  private Integer txId;
  private List<Statement> statements;
  private Integer status;

  @Override
  protected Object clone() throws CloneNotSupportedException {
    return super.clone();
  }
}
