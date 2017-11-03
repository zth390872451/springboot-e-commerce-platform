package com.svlada.filter;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * API访问拦截器
 *
 * @creator ZTH
 * @modifier ZTH
 * @date：2017-05-19
 */
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return ApiLimitInterceptorHandler.ApiLimitHandler(request, response, handler);
    }

}
