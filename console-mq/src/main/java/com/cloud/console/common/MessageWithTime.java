package com.cloud.console.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by Frank on 2019-01-14. */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageWithTime {
  private long id;
  private long time;
  private Object message;
}
