package com.cloud.console;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Frank on 2017/8/3.
 *
 * <p>Spring Boot入口
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = "com.cloud.console.mapper")
@EnableTransactionManagement
public class App extends SpringBootServletInitializer {

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  /*@Bean
  public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
      return args -> {
          System.out.println("Let's inspect the beans provided by Spring Boot:");
          String[] beanNames = ctx.getBeanDefinitionNames();
          Arrays.sort(beanNames);
          for (String beanName : beanNames) {
              System.out.println(beanName);
          }
      };
  }*/
}
