package com.cloud.console.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/** Created by Frank on 2018-12-21. */
@Data
@ApiModel(description = "系统字段参数对象")
public class Dict {
  private Long id;
  @ApiModelProperty("字段名")
  private String dict_name;
  @ApiModelProperty("字段值")
  private String dict_value;
  @ApiModelProperty("使用状态")
  private Integer status;
}
