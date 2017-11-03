package com.svlada.config;


public class WxConfig {
	public static String SUCCESS = "SUCCESS"; //成功return_code
	public static String FAIL = "FAIL";   //失败return_code

	public static final String APP_ID = "wx6aef1915818229a5";//应用ID
	public static final String MCH_ID = "1364053502";//商户号

	public final static String API_KEY = "d21e6a1fb34ccb89504d8b9ba934bc24";//API密钥 06007424c2d77eda0361ae3e0366c4f7
	public static final String TRADE_TYPE = "MWEB";//支付类型：H5

	public static String ACCESS_TOKEN ="";

	//此处TOKEN即我们刚刚所填的token
	public static String TOKEN = "weixin";
	//手动授权
	public static final String SNSAPI_USERINFO_SCOPE = "snsapi_userinfo";


	//用户手动授权
	public static String WEIXIN_MANUAL_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+APP_ID+"&redirect_uri=%s&response_type=code&scope="+SNSAPI_USERINFO_SCOPE+"&#wechat_redirect";

	// 微信支付统一接口(POST)
	public final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//获取AccessToken
	public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APP_ID+"&secret="+API_KEY;
	//获取jssdk的ticket
	public static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";


}
