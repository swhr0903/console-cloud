package com.cloud.console;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Created by Frank on 2017/9/1. MVC配置 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  /**
   * 静态资源配置
   *
   * @param registry
   */
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/static/**")
        .addResourceLocations("file:static/")
        .addResourceLocations("classpath:/static/");
  }
}
