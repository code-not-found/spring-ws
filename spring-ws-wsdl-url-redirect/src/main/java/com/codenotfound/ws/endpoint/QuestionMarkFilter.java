package com.codenotfound.ws.endpoint;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component
public class QuestionMarkFilter implements Filter {

  @Override
  public void doFilter(ServletRequest servletRequest,
      ServletResponse servletResponse, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpServletRequest =
        (HttpServletRequest) servletRequest;
    HttpServletResponse httpServletResponse =
        (HttpServletResponse) servletResponse;

    if (httpServletRequest.getQueryString() != null
        && httpServletRequest.getQueryString().toLowerCase()
            .endsWith("wsdl")) {
      // redirect
      httpServletResponse
          .sendRedirect(httpServletRequest.getContextPath()
              + "/codenotfound/ws/ticketagent.wsdl");
    } else {
      // do nothing
      chain.doFilter(servletRequest, servletResponse);
    }
  }

  @Override
  public void init(FilterConfig filterConfig) {
    // empty
  }

  @Override
  public void destroy() {
    // empty
  }
}
