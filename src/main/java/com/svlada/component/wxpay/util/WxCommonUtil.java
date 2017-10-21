package com.svlada.component.wxpay.util;

import com.svlada.component.wxpay.config.WxConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.UUID;

public class WxCommonUtil {
	private static final Logger log = LoggerFactory.getLogger(WxCommonUtil.class);

	public static String getUuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 签名工具
	 */
	public static String createSign(String characterEncoding, Map<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		Iterator<Entry<String, Object>> it = parameters.entrySet().iterator();
		while (it.hasNext()) {
			Entry <String,Object>entry = (Entry<String,Object>) it.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();//去掉带sign的项
			if (null != value && !"".equals(value) && !"sign".equals(key) && !"key".equals(key)) {
				sb.append(key + "=" + value + "&");
			}
		}
		sb.append("key=" + WxConfig.API_KEY);
		//注意sign转为大写
		return MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
	}

    /**
     * 检验API返回的数据里面的签名是否合法，避免数据在传输的过程中被第三方篡改
     * @param responseStr API返回的XML数据字符串
     */
    public static boolean checkIsSignValidFromResponseString(String responseStr) {
        try {
        	SortedMap<String, Object> map = XMLUtil.doXMLParse(responseStr);
            //Map<String, Object> map = XMLParser.getMapFromXML(responseString);
            log.debug("xml to map object:{}", map.toString());
            String signFromAPIResponse = map.get("sign").toString();
            if ("".equals(signFromAPIResponse) || signFromAPIResponse == null) {
                log.debug("API返回的数据签名数据不存在，有可能被第三方篡改!!!");
                return false;
            }
            //log.debug("服务器回包里面的签名是:" + signFromAPIResponse);
            //清掉返回数据对象里面的Sign数据（不能把这个数据也加进去进行签名），然后用签名算法进行签名
            map.put("sign", "");
            //将API返回的数据根据用签名算法进行计算新的签名，用来跟API返回的签名进行比较
            String signForAPIResponse = WxCommonUtil.createSign("UTF-8", map);
            if (!signForAPIResponse.equals(signFromAPIResponse)) {
                //签名验不过，表示这个API返回的数据有可能已经被篡改了
                log.debug("API返回的数据签名验证不通过，有可能被第三方篡改!!!");
                return false;
            }
            log.debug("wxpay 统一下单回调数据验签通过");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
	 * 返回给微信的参数
	 */
	public static String setXML(String return_code, String return_msg) {
		return "<xml><return_code><![CDATA[" + return_code
				+ "]]></return_code><return_msg><![CDATA[" + return_msg
				+ "]]></return_msg></xml>";
	}

	/**
	 * 将请求参数转换为xml格式的string
	 */
	public static String getRequestXml(SortedMap<String, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Iterator<Entry<String, Object>> iterator = parameters.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String,Object> entry = (Entry<String,Object>) iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key)
					|| "sign".equalsIgnoreCase(key)) {
				sb.append("<" + key + ">" + "<![CDATA[" + value + "]]></" + key + ">");
			} else {
				sb.append("<" + key + ">" + value + "</" + key + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}
	
}
