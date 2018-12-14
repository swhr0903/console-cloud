package com.cloud.console;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/** Created by Frank on 2017/9/18. */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public Map<String, Object> defaultErrorHandler(Exception e) {
    Map<String, Object> result = new HashMap<>();
    result.put("code", 0);
    result.put("msg", e.getMessage());
    return result;
  }
}
