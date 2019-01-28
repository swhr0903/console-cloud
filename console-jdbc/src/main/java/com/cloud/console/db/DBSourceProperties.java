package com.cloud.console.db;

import lombok.Getter;
import lombok.Setter;

/** Created by Frank on 2019-01-12. */
@Getter
@Setter
public class DBSourceProperties {
  private String filters;
  private String connectionProperties;
  private String driverClassName;
  private String url;
  private String username;
  private String password;
  private Integer initialSize;
  private Integer minIdle;
  private Integer maxActive;
  private Integer maxWait;
  private Integer timeBetweenEvictionRunsMillis;
  private Integer minEvictableIdleTimeMillis;
  private String validationQuery;
  private Integer validationQueryTimeout;
  private Boolean testWhileIdle;
  private Boolean testOnBorrow;
  private Boolean testOnReturn;
  private Boolean poolPreparedStatements;
  private Integer maxPoolPreparedStatementPerConnectionSize;
  private boolean useGlobalDataSourceStat;
}
