package com.svlada.component.wxpay.config;


public class WxConfig {
	public static String SUCCESS = "SUCCESS"; //成功return_code
	public static String FAIL = "FAIL";   //失败return_code
	
	public static final String APP_ID = "wx6996e8c920b60b83";//应用ID
	public static final String MCH_ID = "1364053502";//商户号
	
	public final static String API_KEY = "06007424c2d77eda0361ae3e0366c4f7";//API密钥 06007424c2d77eda0361ae3e0366c4f7
	public static final String TRADE_TYPE = "MWEB";//支付类型：H5
	
	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
}
