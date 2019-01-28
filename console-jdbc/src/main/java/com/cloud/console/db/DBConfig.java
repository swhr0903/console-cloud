package com.cloud.console.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.cloud.console.common.PojoUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/** Created by Frank on 2029-02-22. */
@Configuration
public class DBConfig {

  @Bean
  @ConfigurationProperties(prefix = "spring.datasource.druid")
  public DBSourceProperties dbSourceProperties() {
    return new DBSourceProperties();
  }

  @Bean("druidDataSource")
  public DataSource druidDataSource() {
    DruidDataSource druidDataSource = new DruidDataSource();
    druidDataSource.setConnectProperties(PojoUtils.pojo2Prop(dbSourceProperties()));
    return druidDataSource;
  }

  @Bean("sqlSessionFactory")
  public SqlSessionFactory sqlSessionFactory(@Qualifier("druidDataSource") DataSource dataSource)
      throws Exception {
    SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
    sqlSessionFactoryBean.setDataSource(dataSource);
    return sqlSessionFactoryBean.getObject();
  }

  @Bean
  public SqlSessionTemplate sqlSessionTemplate(
      @Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    SqlSessionTemplate sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    return sqlSessionTemplate;
  }

  @Bean
  public DataSourceTransactionManager dataSourceTransactionManager(
      @Qualifier("druidDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }
}
