package com.cloud.console.mq;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Created by Frank on 2019-01-14. */
@Configuration
public class MQConfig {

  @Bean
  @ConfigurationProperties(prefix = "mq")
  public MQProperties mqProperties() {
    return new MQProperties();
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory =
        new CachingConnectionFactory(mqProperties().getHost(), mqProperties().getPort());
    connectionFactory.setUsername(mqProperties().getUserName());
    connectionFactory.setPassword(mqProperties().getPassword());
    connectionFactory.setVirtualHost(mqProperties().getVirtualHost());
    connectionFactory.setPublisherConfirms(true);
    return connectionFactory;
  }
}
