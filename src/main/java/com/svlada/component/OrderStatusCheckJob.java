package com.svlada.component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.svlada.component.wxpay.api.WxClient;
import com.svlada.component.wxpay.config.WxConfig;
import com.svlada.endpoint.wechat.util.HttpsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.svlada.component.wxpay.config.WxConfig.ACCESS_TOKEN_URL;


@Component
public class OrderStatusCheckJob {

	private static final Logger logger = LoggerFactory.getLogger(WxClient.class);


	// cron表达式：每个一小时执行一次
	@Scheduled(cron="0 0 */1 * * ?")
//	@Scheduled(cron="0 */1 * * * ?")
	public void getAccessToken(){
		try {
			String response = HttpsUtil.httpsRequestToString(ACCESS_TOKEN_URL, "GET", null);
			JSONObject jsonObject = JSON.parseObject(response);
			String access_token = jsonObject.getString("access_token");
			logger.info("请求到的 jsonObject:{}", jsonObject.toJSONString());
			if (access_token!=null){
				WxConfig.ACCESS_TOKEN = access_token;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
