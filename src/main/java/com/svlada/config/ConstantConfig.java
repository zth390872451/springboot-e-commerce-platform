package com.svlada.config;

public class ConstantConfig {
    //正式
    public static final String back_host  = "http://www.dsunyun.com";
    //本地
//    public static final String back_host  = "http://127.0.0.1";

    //购物车中商品的详情图片 若没有，则设置该链接为默认图片
    public static final String DEFAULT_PIC = back_host+"/drink/default.png";

    public static final String font_host = "http://www.dsunyun.com:81";

    public static final String index = font_host +"/drink/index?openId=%s&jwtToken=%s";
    public static final String address = font_host +"/drink/address";
    public static final String car = font_host +"/drink/car";
    public static final String myself = font_host +"/drink/myself";


}
