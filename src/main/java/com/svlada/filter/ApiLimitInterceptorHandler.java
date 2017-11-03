package com.svlada.filter;


import com.svlada.config.WxConfig;
import com.svlada.common.utils.wx.OAuthUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;


/**
 * 微信授权 拦截器处理类
 * @creator ZTH
 * @modifier ZTH
 * @date：2017-05-19
 */
public class ApiLimitInterceptorHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(ApiLimitInterceptorHandler.class);
    /**
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    public static boolean ApiLimitHandler(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        Enumeration em = request.getParameterNames();
        while (em.hasMoreElements()) {
            String name = (String) em.nextElement();
            String value = request.getParameter(name);
            LOGGER.info("请求参数 name:{},value{}",name,value);
        }
        String requestURI = request.getRequestURI();//半路径
        if (requestURI.startsWith("/drink")){//需要微信授权的页面
            String code = request.getParameter("code");
            String requestURL = request.getRequestURL().toString();
            String encodeUrl = URLEncoder.encode(requestURL);//编码后的全路径
            if (StringUtils.isEmpty(code)){
                LOGGER.info("未授权,重定向到授权页面");
                String targetUrl = String.format(WxConfig.WEIXIN_MANUAL_URL,encodeUrl);//授权url
                LOGGER.info("请求参数 encodeUrl:{}",encodeUrl);
                LOGGER.info("请求参数 targetUrl:{}",targetUrl);
                response.sendRedirect(targetUrl);
                LOGGER.info("sendRedirect调用完毕");
                return false;
            }else {//已经获取了授权code
                LOGGER.info("已经获取了授权code={},准备获取用户信息",code);
                OAuthUtils.oauth2(requestURL,code);
                LOGGER.info("成功获取到了用户信息，进入目标接口!");
                return true;
            }
        }else {//不需要微信授权的页面
            return true;
        }
    }

}
