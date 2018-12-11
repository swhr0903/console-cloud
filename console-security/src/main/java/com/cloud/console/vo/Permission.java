package com.cloud.console.vo;

import lombok.Data;

import java.util.List;

/** Created by Frank on 2017/9/6. */
@Data
public class Permission {
  private Long moduleId;
  private List<String> options;
}
