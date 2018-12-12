package com.cloud.console;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/** Created by Frank on 2017/9/18. */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public Map<String, String> defaultErrorHandler(HttpServletRequest req, Exception e) {
    Map<String, String> result = new HashMap<>();
    result.put("0", e.getMessage());
    return result;
  }
}
