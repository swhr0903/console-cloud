package com.cloud.console.vo;

import lombok.Data;

import java.io.Serializable;

/** Created by Frank on 2017/8/3. */
@Data
public class User implements Serializable {
  private String username;
  private String password;
  private String startTime;
  private String endTime;
  private Integer status;
}
