package com.cloud.console.vo;

import com.cloud.console.service.Options;
import lombok.Data;

/** Created by Frank on 2017/8/4. */
@Data
public class Module {
  private Long id;
  private String name;
  private String url;
  private String parent;
  private String options;
  private Integer is_leaf;
  private Integer status;

  public void setOptions(String options) {
    StringBuilder stringBuilder = new StringBuilder();
    String[] optionArray = options.split(",");
    int i = 0;
    for (String option : optionArray) {
      stringBuilder
          .append(Options.getNameByCode(option))
          .append(i < optionArray.length - 1 ? "," : "");
      i++;
    }
    this.options = stringBuilder.toString();
  }
}
