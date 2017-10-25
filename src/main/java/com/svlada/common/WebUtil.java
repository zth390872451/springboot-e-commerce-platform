package com.svlada.common;

import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.UserRepository;
import com.svlada.entity.User;
import com.svlada.security.model.UserContext;
import com.svlada.security.model.token.JwtToken;
import com.svlada.security.model.token.JwtTokenFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
public class WebUtil {
    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "X-Requested-With";

    private static final String CONTENT_TYPE = "Content-type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    public static boolean isAjax(HttpServletRequest request) {
        return XML_HTTP_REQUEST.equals(request.getHeader(X_REQUESTED_WITH));
    }

    public static boolean isAjax(SavedRequest request) {
        return request.getHeaderValues(X_REQUESTED_WITH).contains(XML_HTTP_REQUEST);
    }

    public static boolean isContentTypeJson(SavedRequest request) {
        return request.getHeaderValues(CONTENT_TYPE).contains(CONTENT_TYPE_JSON);
    }

    //当前登录的用户
    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static void setCurrentUser(User user){
        userThreadLocal.set(user);
    }

    public static User getCurrentUser(){
        User user = userThreadLocal.get();
        if (user==null){
            user = new User();
            user.setId(1L);
        }
        return user;
    }


    public static String createTokenByOpenId(String openId){
        UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
        JwtTokenFactory jwtTokenFactory = ApplicationSupport.getBean(JwtTokenFactory.class);
        User user = userRepository.findOneByOpenId(openId);
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getRole().authority()))
                .collect(Collectors.toList());
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        JwtToken jwtToken = jwtTokenFactory.createAccessJwtToken(userContext);
        String token = jwtToken.getToken();
        user.setJwtToken(token);
        userRepository.save(user);
        return token;
    }
}
