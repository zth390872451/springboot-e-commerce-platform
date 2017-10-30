package com.svlada.component.wxpay.config;


public class WxConfig {
	public static String SUCCESS = "SUCCESS"; //成功return_code
	public static String FAIL = "FAIL";   //失败return_code
	
	public static final String APP_ID = "wx6aef1915818229a5";//应用ID
	public static final String MCH_ID = "1364053502";//商户号
	
	public final static String API_KEY = "d21e6a1fb34ccb89504d8b9ba934bc24";//API密钥 06007424c2d77eda0361ae3e0366c4f7
	public static final String TRADE_TYPE = "MWEB";//支付类型：H5

	public static String ACCESS_TOKEN ="";

	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+API_KEY;

	public static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";


}
