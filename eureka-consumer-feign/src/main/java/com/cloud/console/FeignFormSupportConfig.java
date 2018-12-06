package com.cloud.console;

import feign.codec.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Frank on 2018-12-06.
 */
@Configuration
public class FeignFormSupportConfig {

    @Bean
    public Encoder feignFormEncoder() {
        return new FormEncoder();
    }
}
