package com.cloud.console;

import com.cloud.console.common.Constant;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/** Created by Frank on 2017/9/18. */
@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(value = Exception.class)
  public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {
    ModelAndView mav = new ModelAndView();
    mav.addObject("exception", e);
    mav.addObject("url", request.getRequestURL());
    mav.setViewName(Constant.DEFAULT_ERROR_VIEW);
    return mav;
  }
}
