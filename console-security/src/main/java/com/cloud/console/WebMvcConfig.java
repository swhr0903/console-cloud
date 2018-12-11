package com.cloud.console;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/** Created by Frank on 2017/9/1. MVC配置 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  /**
   * 指定FastJson为JSON处理器
   *
   * @param converters
   */
  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    fastConverter.setFastJsonConfig(fastJsonConfig);
    converters.add(0, fastConverter);
  }

  /**
   * 增加刷新token的拦截器
   *
   * @param registry
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new AutoRefeshTokenHandler()).addPathPatterns("/**");
  }
}
