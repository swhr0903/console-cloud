package com.cloud.console;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import io.jmnarloch.spring.cloud.ribbon.support.RibbonFilterContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/** Created by Frank on 2018-12-10. */
@Component
public class UrlRedirectFilter extends ZuulFilter {

  @Override
  public int filterOrder() {
    return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
  }

  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE;
  }

  @Override
  public boolean shouldFilter() {
    RequestContext ctx = RequestContext.getCurrentContext();
    return !ctx.containsKey(FilterConstants.FORWARD_TO_KEY)
        && !ctx.containsKey(FilterConstants.SERVICE_ID_KEY);
  }

  @Override
  public Object run() {
    RequestContext ctx = RequestContext.getCurrentContext();
    HttpServletRequest request = ctx.getRequest();
    String version = request.getHeader("version");
    if (StringUtils.isNotBlank(version)) {
      RibbonFilterContextHolder.getCurrentContext().add("version", version);
    }
    return null;
  }
}
