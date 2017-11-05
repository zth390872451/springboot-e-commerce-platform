package com.svlada.endpoint.wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.ApplicationSupport;
import com.svlada.common.utils.wx.HttpsUtil;
import com.svlada.common.utils.wx.UserInfoUtil;
import com.svlada.component.repository.PartnerRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.config.ConstantConfig;
import com.svlada.config.WxConfig;
import com.svlada.entity.Partner;
import com.svlada.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.success;

/**
 * 需要进行微信授权的页面
 * 主页 drink/index  授权成功，后端跳转 81/drink/index
 * 地址 drink/address  授权成功，后端跳转 81/drink/address
 * 购物车 drink/car  授权成功，后端跳转 81/drink/car
 * 个人中心 drink/myself  授权成功，后端跳转 81/drink/myself
 */
@Controller
@RequestMapping("/drink")
public class RedirectResource {

    private Logger LOGGER = LoggerFactory.getLogger(getClass());
    @Autowired
    private PartnerRepository partnerRepository;
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/index")
    public void index(RedirectAttributes redirectAttributes,
                      HttpServletResponse response) throws IOException {
        User currentUser = WebUtil.getCurrentUser();
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.index, openId, jwtToken);
        LOGGER.info("index openId={}", openId);
        LOGGER.info("index jwtToken={}", jwtToken);
        LOGGER.info("index redirect={}", redirect);
        response.sendRedirect(redirect);
    }

    @GetMapping("/address")
    public void address(HttpServletResponse response) throws IOException {
        LOGGER.info("address redirect={}", ConstantConfig.address);
        response.sendRedirect(ConstantConfig.address);
    }

    @GetMapping("/car")
    public void car(HttpServletResponse response) throws IOException {
        LOGGER.info("car redirect={}", ConstantConfig.car);
        response.sendRedirect(ConstantConfig.car);
    }

    @GetMapping("/myself")
    public void myself(HttpServletResponse response) throws IOException {
        LOGGER.info("address redirect={}", ConstantConfig.myself);
        response.sendRedirect(ConstantConfig.myself);
    }

    /**
     * 好友分享 功能
     */
    @GetMapping("/share")
    public void JSSDK_config(
            @RequestParam(value = "shareOpenId") String shareOpenId,
            HttpServletResponse response) throws IOException {
        User currentUser = WebUtil.getCurrentUser();
        User shareUser = userRepository.findOneByOpenId(shareOpenId);//分享人
        if (shareUser == null) {
            response.sendRedirect(ConstantConfig.error_404);
        }
        Long userId = shareUser.getId();
        //查看该用户是否已经有过合作伙伴
        Partner partner = partnerRepository.findOneByUserId(userId);
        if (partner == null) {//没有，则添加分享人为合作伙伴
            partner = new Partner();
            partner.setShareUser(shareUser);
            partner.setUserId(currentUser.getId());
            partnerRepository.save(partner);
        }else {//已经有合作伙伴不做处理

        }
        String openId = currentUser.getOpenId();
        String jwtToken = WebUtil.createTokenByOpenId(openId);
        String redirect = String.format(ConstantConfig.index, openId, jwtToken);
        LOGGER.info("index openId={}", openId);
        LOGGER.info("index jwtToken={}", jwtToken);
        LOGGER.info("index redirect={}", redirect);
        LOGGER.info("address redirect={}", ConstantConfig.index);
        response.sendRedirect(ConstantConfig.myself);
    }

    @GetMapping("/")
    public void login(@RequestParam(name = "code", required = false) String code,
                      @PathVariable(name = "url", required = false) String url,
                      @RequestParam(name = "state", required = false) String state,
                      RedirectAttributes redirectAttributes,
                      HttpServletResponse response) throws IOException {
        LOGGER.info("授权成功!");
        User currentUser = WebUtil.getCurrentUser();
        if (currentUser != null) {
            String jwtToken = WebUtil.createTokenByOpenId(currentUser.getOpenId());
            LOGGER.info("获取 jwtToken={}", jwtToken);
//            response.sendRedirect("/index.html");
//            redirect:http://www.dsunyun.com:81/drink?openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken
            response.sendRedirect("http://www.dsunyun.com:81/drink?openId=" + currentUser.getOpenId() + "&access_token=" + currentUser.getWebAccessToken() + "&jwtToken=" + jwtToken);
        } else {
            response.sendRedirect("/index.html");
//            response.sendRedirect("http://www.dsunyun.com:81/drink");
        }
//            return "redirect:http://www.dsunyun.com:81/drink?openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken;
    }


    @GetMapping("/test3")
    public void test3(@RequestParam(name = "code", required = false) String code,
                      @PathVariable(name = "url", required = false) String url,
                      @RequestParam(name = "state", required = false) String state,
                      RedirectAttributes redirectAttributes,
                      HttpServletResponse response) throws IOException {
        response.sendRedirect("http://www.dsunyun.com:81/drink");
        return;
    }

    @GetMapping("/test4")
    @ResponseBody
    public String test4(@RequestParam(name = "code", required = false) String code,
                        @PathVariable(name = "url", required = false) String url,
                        @RequestParam(name = "state", required = false) String state,
                        RedirectAttributes redirectAttributes,
                        HttpServletResponse response) throws IOException {
        User currentUser = WebUtil.getCurrentUser();
//        response.sendRedirect("/index.html");
        if (currentUser != null) {
            return currentUser.getOpenId();
        } else {
            return "未获取到user";
        }

    }

    @GetMapping("/test5")
    public void test5(@RequestParam(name = "code", required = false) String code,
                      @PathVariable(name = "url", required = false) String url,
                      @RequestParam(name = "state", required = false) String state,
                      RedirectAttributes redirectAttributes,
                      HttpServletResponse response) throws IOException {
        response.sendRedirect("/index.html");
        return;
    }


    /**
     * 微信网页授权流程:
     * 1. 用户同意授权,获取 code
     * 2. 通过 code 换取网页授权 access_token
     * 3. 使用获取到的 access_token 和 openid 拉取用户信息
     *
     * @param code  用户同意授权后,获取到的code
     * @param state 重定向状态参数
     * @return
     */
    @RequestMapping("/url")
    public String wechatLogin(@RequestHeader(name = "accessToken", required = false) String accessToken,
                              @RequestParam(name = "code", required = false) String code,
                              @RequestParam(name = "state", required = false, defaultValue = "STATE") String state
            , RedirectAttributes attributes) {
        if (StringUtils.isEmpty(code)) {//用户尚未授权
            LOGGER.info("用户尚未授权,准备进入提示授权页面.");
            return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6aef1915818229a5&redirect_uri=http%3A%2F%2Fwww.dsunyun.com%2Furl&response_type=code&scope=snsapi_userinfo&#wechat_redirect";
        }

        // 1. 用户同意授权,获取code
        LOGGER.info("收到微信重定向跳转.");
        LOGGER.info("用户同意授权,获取code:{} , state:{}", code, state);

        // 2. 通过code换取网页授权access_token
        if (!StringUtils.isEmpty(code)) {
            String APPID = WxConfig.APP_ID;
            String SECRET = WxConfig.API_KEY;
            String CODE = code;
            String WebAccessToken = "";
            String openId = "";
            String nickName, sex, openid = "";
            String REDIRECT_URI = "http://www.dsunyun.com/url";
            String SCOPE = "snsapi_userinfo";

            String getCodeUrl = UserInfoUtil.getCode(APPID, REDIRECT_URI, SCOPE);
            LOGGER.info("第一步:用户授权, get Code URL:{}", getCodeUrl);

            // 替换字符串，获得请求access token URL
            String tokenUrl = UserInfoUtil.getWebAccess(APPID, SECRET, CODE);
            LOGGER.info("第二步:get Access Token URL:{}", tokenUrl);

            // 通过https方式请求获得web_access_token
            String response = HttpsUtil.httpsRequestToString(tokenUrl, "GET", null);

            JSONObject jsonObject = JSON.parseObject(response);
            LOGGER.info("请求到的Access Token:{}", jsonObject.toJSONString());
            if (null != jsonObject) {
                try {
                    WebAccessToken = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
                    LOGGER.info("获取access_token成功!");
                    LOGGER.info("WebAccessToken:{} , openId:{}", WebAccessToken, openId);
                    // 3. 使用获取到的 Access_token 和 openid 拉取用户信息
                    String userMessageUrl = UserInfoUtil.getUserMessage(WebAccessToken, openId);
                    LOGGER.info("第三步:获取用户信息的URL:{}", userMessageUrl);

                    // 通过https方式请求获得用户信息响应
                    String userMessageResponse = HttpsUtil.httpsRequestToString(userMessageUrl, "GET", null);

                    JSONObject userMessageJsonObject = JSON.parseObject(userMessageResponse);
                    LOGGER.info("用户信息:{}", userMessageJsonObject.toJSONString());
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

                            LOGGER.info("用户昵称:{}", nickName);
                            LOGGER.info("用户性别:{}", sex);
                            LOGGER.info("OpenId:{}", openid);

                            UserRepository userRepository = ApplicationSupport.getBean(UserRepository.class);
                            User user = userRepository.findOneByOpenId(openId);
                            if (user == null) {
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
                            LOGGER.error("获取用户信息失败 failed");
                            return "failed!";
                        }
                    }
                } catch (JSONException e) {
                    LOGGER.error("获取Web Access Token失败");
                    return "failed!";
                }
            }
            LOGGER.info("授权成功!");
            String jwtToken = WebUtil.createTokenByOpenId(openId);
            return "redirect:index.html";
//            return "redirect:http://www.dsunyun.com:81/drink?openId="+openId+"&access_token="+WebAccessToken+"&jwtToken="+jwtToken;
        }
        LOGGER.info("end 尚未授权,即将调到微信的授权页面");
        return "redirect:https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx6aef1915818229a5&redirect_uri=http%3A%2F%2Fwww.dsunyun.com%2Fdrink&response_type=code&scope=snsapi_userinfo&#wechat_redirect";
    }

    @RequestMapping("/get/token")
    @ResponseBody
    public CustomResponse getJwtToken(@RequestParam(name = "openId", required = false) String openId) {
        User user = userRepository.findOneByOpenId(openId);
        Map<String, Object> result = new HashMap<>();
        result.put("jwtToken", user.getJwtToken());
        return success(result);
    }

    @RequestMapping("/test")
    public String test(@RequestParam(name = "openId", required = false) String openId
            , RedirectAttributes attributes) {
        String token = "test";
        return "redirect:index.html?token=" + token;
    }




}
