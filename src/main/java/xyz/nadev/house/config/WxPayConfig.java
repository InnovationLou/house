package xyz.nadev.house.config;

public class WxPayConfig {
    public static final String APP_ID = "wxf23c68d0aea36deb";
    public static final String APP_SECRET = "abbb49494ab61bbd3d0abc9f7bcca309";
    public static final String MCH_ID = "1567855211";
    public static final String MCH_SECRET = "qazxswedcvfr123454321hKNJUijOP6G";

    //回调地址  用于查看小程序回调信息
    public static final String NOTIFY_URL = "127.0.0.1:8080/pay/wxNotify";
    //微信支付地址：
    public static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String TRADE_TYPE = "JSAPI";
    public static final String BODY = "小鱼公寓-充值订单-微信小程序";
    public static final String SIGN_TYPE = "MD5";
}
