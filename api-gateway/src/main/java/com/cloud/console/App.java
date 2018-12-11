package com.cloud.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/** Created by Frank on 2018/5/16. */
@EnableZuulProxy
@SpringBootApplication
@MapperScan(basePackages = "com.cloud.console.mapper")
public class App extends SpringBootServletInitializer {
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
