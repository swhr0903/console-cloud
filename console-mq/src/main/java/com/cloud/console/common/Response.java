package com.cloud.console.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Created by Frank on 2019-01-14. */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
  private Boolean isSuccess;
  private String errMsg;
}
