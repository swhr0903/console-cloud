package com.cloud.console;

import com.netflix.hystrix.exception.HystrixTimeoutException;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/** Created by Frank on 2018-12-15. */
@Component
public class ApiFallbackProvider implements FallbackProvider {
  @Override
  public String getRoute() {
    return "*";
  }

  @Override
  public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
    String message;
    if (cause instanceof HystrixTimeoutException) {
      message = "Timeout";
    } else {
      message = "Service exception";
    }
    return fallbackResponse(message);
  }

  public ClientHttpResponse fallbackResponse(String message) {

    return new ClientHttpResponse() {
      @Override
      public HttpStatus getStatusCode() {
        return HttpStatus.OK;
      }

      @Override
      public int getRawStatusCode() {
        return 200;
      }

      @Override
      public String getStatusText() {
        return "OK";
      }

      @Override
      public void close() {}

      @Override
      public InputStream getBody() {
        String bodyText = String.format("{\"code\": 999,\"msg\": \"服务不可用:%s\"}", message);
        return new ByteArrayInputStream(bodyText.getBytes());
      }

      @Override
      public HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
      }
    };
  }
}
