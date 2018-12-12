package com.cloud.console;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/** Created by Frank on 2018-12-10. */
//@Component
public class UrlRedirectFilter extends ZuulFilter {

  private static Map<String, String> urlMap = new HashMap<>();

  static {
    urlMap.put("t", "/user/");
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String url = request.getRequestURI();
    String[] split = url.split("/", 3);
    if (split.length >= 2) {
      String val = urlMap.get(split[1]);
      if (StringUtils.isNotEmpty(val)) {
        url = url.replaceFirst("/" + split[1] + "/", val);
        ctx.put(FilterConstants.REQUEST_URI_KEY, url);
      }
    }
    return null;
  }

  @Override
  public boolean shouldFilter() {
    return true;
  }

  @Override
  public int filterOrder() {
    return 1;
  }

  @Override
  public String filterType() {
    return FilterConstants.ROUTE_TYPE;
  }
}
