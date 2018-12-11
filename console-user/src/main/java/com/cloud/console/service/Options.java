package com.cloud.console.service;

/** Created by Frank on 2017/9/6. */
public enum Options {
  ALL("all", "列表"),
  ADD("add", "新增"),
  UPDATE("update", "修改"),
  DEL("del", "删除"),
  QUERY("query", "查询"),
  EXPORT("export", "导出"),
  AUTH("auth", "授权");
  private String code;
  private String name;

  Options(String code, String name) {
    this.code = code;
    this.name = name;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public static String getNameByCode(String code) {
    for (Options option : Options.values()) {
      if ((option.getCode()).equals(code)) {
        return option.getName();
      }
    }
    return null;
  }

  public static String getCodeByName(String name) {
    for (Options option : Options.values()) {
      if ((option.getName()).equals(name)) {
        return option.getCode();
      }
    }
    return null;
  }
}
