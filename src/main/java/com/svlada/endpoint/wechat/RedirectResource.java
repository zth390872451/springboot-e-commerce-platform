package com.svlada.endpoint.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.UserRepository;
import com.svlada.component.wxpay.util.WxCommonUtil;
import com.svlada.endpoint.wechat.util.HttpsUtil;
import com.svlada.endpoint.wechat.util.UserInfoUtil;
import com.svlada.entity.User;
import com.svlada.security.model.token.JwtTokenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.success;

@Controller
public class RedirectResource {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static final String WX_APPID = "wx6aef1915818229a5";
    public static final String WX_APPSECRET = "d21e6a1fb34ccb89504d8b9ba934bc24";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    /**
     * 微信网页授权流程:
     * 1. 用户同意授权,获取 code
     * 2. 通过 code 换取网页授权 access_token
     * 3. 使用获取到的 access_token 和 openid 拉取用户信息
     * @param code  用户同意授权后,获取到的code
     * @param state 重定向状态参数
     * @return
     */
    @RequestMapping("/drink")
    public String wechatLogin(@RequestHeader(name = "accessToken", required = false) String accessToken,
                              @RequestParam(name = "code", required = false) String code,
                              @RequestParam(name = "redirect_uri", required = false) String redirect_uri,
                              @RequestParam(name = "state",required = false,defaultValue = "STATE") String state,
                            HttpServletRequest httpServletRequest) {
        Map<String, String[]> parameterMap = httpServletRequest.getParameterMap();
        logger.info("请求参数：parameterMap:{}",parameterMap.values());
        if (StringUtils.isEmpty(code)){//用户尚未授权
            logger.info("用户尚未授权,准备进入提示授权页面.");
        }

        // 1. 用户同意授权,获取code
        logger.info("收到微信重定向跳转.");
        logger.info("用户同意授权,获取code:{} , state:{}", code, state);

        // 2. 通过code换取网页授权access_token
        if (!StringUtils.isEmpty(code)) {
            String APPID = WX_APPID;
            String SECRET = WX_APPSECRET;
            String CODE = code;
            String WebAccessToken = "";
            String openId = "";
            String nickName,sex,openid = "";
            String REDIRECT_URI = "http://www.dsunyun.com/drink";
            String SCOPE = "snsapi_userinfo";

            String getCodeUrl = UserInfoUtil.getCode(APPID, redirect_uri, SCOPE);
            logger.info("第一步:用户授权, get Code URL:{}", getCodeUrl);

            // 替换字符串，获得请求access token URL
            String tokenUrl = UserInfoUtil.getWebAccess(APPID, SECRET, CODE);
            logger.info("第二步:get Access Token URL:{}", tokenUrl);

            // 通过https方式请求获得web_access_token
            String response = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

            JSONObject jsonObject = JSON.parseObject(response);
            logger.info("请求到的Access Token:{}", jsonObject.toJSONString());
            if (null != jsonObject) {
                try {
                    WebAccessToken = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                    logger.info("获取access_token成功!");
                    logger.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);
                    // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                    String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    logger.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                    // 通过https方式请求获得用户信息响应
                    String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

                    JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);
                    logger.info("用户信息:{}", userMessageJsonObject.toJSONString());
                    if (userMessageJsonObject != null) {
                        try {
                            //用户昵称
                            nickName = userMessageJsonObject.getString("nickname");
                            //用户性别
                            sex = userMessageJsonObject.getString("sex");
                            sex = (sex.equals("1")) ? "男" : "女";
                            //用户唯一标识
                            openid = userMessageJsonObject.getString("openid");
                            String headImgUrl = userMessageJsonObject.getString("headimgurl");
                            String province = userMessageJsonObject.getString("province");
                            String city = userMessageJsonObject.getString("city");

                            logger.info("用户昵称:{}", nickName);
                            logger.info("用户性别:{}", sex);
                            logger.info("OpenId:{}", openid);

                            UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
                            User user = userRepository.findOneByOpenId(openId);
                            if (user==null){
                                user = new User();
                            }
                            user.setHeadImgUrl(headImgUrl);
                            user.setSex(sex);
                            user.setProvince(province);
                            user.setCity(city);
                            user.setOpenId(openId);
                            user.setUsername(openId);
                            user.setNickName(nickName);
                            user.setSex(sex);
                            user.setProvince(province);
                            user.setCity(city);
                            userRepository.save(user);
                        } catch (JSONException e) {
                            logger.error("获取用户信息失败 failed");
                            return "failed!";
                        }
                    }
                } catch (JSONException e) {
                    logger.error("获取Web Access Token失败");
                    return "failed!";
                }
            }
            logger.info("授权成功!");
            String jwtToken = WebUtil.createTokenByOpenId(openId);
            return "redirect:"+REDIRECT_URI+"openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken;
//            return "redirect:http://www.dsunyun.com:81?openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken;
        }
        logger.info("尚未授权,即将调到微信的授权页面");
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6aef1915818229a5&redirect_uri=http%3A%2F%2Fwww.dsunyun.com&response_type=code&scope=snsapi_userinfo&#wechat_redirect";
    }

    @RequestMapping("/get/token")
    @ResponseBody
    public CustomResponse getJwtToken(@RequestParam(name = "openId", required = false) String openId) {
        User user = userRepository.findOneByOpenId(openId);
        Map<String,Object> result = new HashMap<>();
        result.put("jwtToken",user.getJwtToken());
        return success(result);
    }

    @RequestMapping("/test")
    public String test(@RequestParam(name = "openId", required = false) String openId
            ,RedirectAttributes attributes) {
        String token = WebUtil.createTokenByOpenId(openId);
        return "redirect:index.html?token="+token;
    }


}
