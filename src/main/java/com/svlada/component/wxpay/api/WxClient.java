package com.svlada.component.wxpay.api;

import com.google.common.collect.ImmutableMap;
import com.svlada.component.wxpay.config.WxConfig;
import com.svlada.component.wxpay.util.MapUtils;
import com.svlada.component.wxpay.util.TimeUtil;
import com.svlada.component.wxpay.util.WxCommonUtil;
import com.svlada.component.wxpay.util.XMLUtil;
import com.svlada.endpoint.dto.TradeDTO;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.SortedMap;

public class WxClient {
	private static final Logger log = LoggerFactory.getLogger(WxClient.class);
	private RestTemplate restTemplate = new RestTemplate();
	

	// 统一下订单

	/**
	 *
	 * @param outTradeNo 商户订单号
	 * @param body 商品描述
	 * @param detail 商品详情
	 * @param totalFee 标价金额
	 * @param ip
	 * @param notifyUrl 通知地址
	 * @return
	 */
	public TradeDTO unifiedOrder(String outTradeNo, String body, String detail, int totalFee, String ip, String notifyUrl) {
		SortedMap<String, Object> parameters = null;
		try {
			parameters = prepareOrder(outTradeNo, body, detail,totalFee, ip, notifyUrl);
			parameters.put("sign", WxCommonUtil.createSign("UTF-8", parameters));// sign签名key
			String requestXML = WxCommonUtil.getRequestXml(parameters);// 生成xml格式字符串
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-type", "text/xml; charset=utf-8");// 设置编码  这个一定不能去
			HttpEntity<String> request = new HttpEntity<String>(requestXML, headers);
			String responseStr = restTemplate.postForObject(WxConfig.UNIFIED_ORDER_URL , request, String.class);// post 请求
	
			// 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
			if (!WxCommonUtil.checkIsSignValidFromResponseString(responseStr)) {
				responseStr = new String(responseStr.getBytes("iso-8859-1"), "utf-8");//解决中文乱码问题
				log.error("wxpay 统一下单回调返回参数", responseStr);
				log.error("wxpay sign valid fail,message :{}", "微信统一下单失败,签名可能被篡改");
				return null;
			}
			// 解析结果 resultStr
			SortedMap<String, Object> resutlMap = XMLUtil.doXMLParse(responseStr);
			SortedMap<String, Object> map = buildClientJson(resutlMap);
			map.put("outTradeNo", outTradeNo);
			return new TradeDTO(outTradeNo, map);
		} catch (JDOMException | IOException e) {
			log.error("wxpay 预支付失败,outTradeNO:{},body:{}",parameters.get("out_trade_no"),parameters.get("body"));
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成订单信息
	 */
	private SortedMap<String, Object> prepareOrder(String outTradeNo,String body, String detail,
			int totalFee, String ip, String notifyUrl) {
		Map<String, Object> map = ImmutableMap.<String, Object> builder()
				.put("appid", WxConfig.APP_ID) // 应用id
				.put("mch_id", WxConfig.MCH_ID) // 商户号
				.put("nonce_str", WxCommonUtil.getUuid()) // 随机字符串
				.put("body", body) // 商品描述
				.put("detail", detail) // 商品详情
				.put("out_trade_no", outTradeNo)// 商户订单号
				.put("total_fee", totalFee) // 总金额(单位：分)
				.put("spbill_create_ip", ip) // 终端IP
				.put("time_start", TimeUtil.getCurrentTimeStr(TimeUtil.FORMAT_YMD_HMS)) // 交易起始时间
				.put("time_expire", TimeUtil.getCurrentTimeNextMinute(35)) // 交易结束时间
				.put("notify_url", notifyUrl) // 通知地址
				.put("trade_type", WxConfig.TRADE_TYPE) // 交易类型
				.build();
		return MapUtils.sortMap(map);
	}

	
	/**
	 * 生成预付快订单完成，返回给android,ios唤起微信所需要的参数。
	 */
	private SortedMap<String, Object> buildClientJson(
			Map<String, Object> resutlMap) throws UnsupportedEncodingException {
		// 获取微信返回的签名
		Map<String, Object> params = ImmutableMap.<String, Object> builder()
				.put("appid", WxConfig.APP_ID)
				.put("noncestr", WxCommonUtil.getUuid())
				.put("package", "Sign=WXPay")
				.put("partnerid", WxConfig.MCH_ID)
				.put("prepayid", resutlMap.get("prepay_id"))
				.put("timestamp", TimeUtil.getTimeStamp()).build();
		// key ASCII排序
		SortedMap<String, Object> sortMap = MapUtils.sortMap(params);
		sortMap.put("package", "Sign=WXPay");
		// paySign的生成规则和Sign的生成规则同理
		String paySign = WxCommonUtil.createSign("UTF-8", sortMap);
		sortMap.put("sign", paySign);
		//--------------------上述签名参数不变
		//--------------------package 转 wxpackage
		sortMap.remove("package");
		sortMap.put("wxpackage", "Sign=WXPay");
		return sortMap;
	}
}
