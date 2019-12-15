package xyz.nadev.house.vo;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信 企业付款 到 个人
 */
public class TransferDto {
    // 与商铺号关联的应用的appid,这里面指的是小程序的id
    private String mch_appid;

    // 微信支付分配的商铺号
    private String mchid;

    //商户订单号 需要保持唯一(支付失败时由该订单号保持状态，若此号改变，则可能产生重复支付的情况)
    private String partner_trade_no;

    //用户的openid 众所周知 每个用户使用的每个小程序的openid不一样
    private String openid;

    //校验真实姓名 NO_CHECK不校验   FORCE_CHECK强制校验
    private String check_name = "NO_CHECK";

    //企业付款金额数 单位是分
    private int amount;

    //随机字符串, 不长于32位
    private String non_str;

    // 商户名称, 如'XXX服务号'
    private String mch_name;

    //注：key为商户平台设置的密钥key
    private String appkey;// ? ? ?

    //企业付款备注
    private String desc;

    private String spbill_create_ip = "45.40.193.214";

    public String getMch_name() {
        return mch_name;
    }

    public void setMch_name(String mch_name) {
        this.mch_name = mch_name;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNon_str() {
        return non_str;
    }

    public void setNon_str(String non_str) {
        this.non_str = non_str;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Transient
    public Map<String, String> map()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mch_appid", this.mch_appid);
        map.put("mchid", this.mchid);
        map.put("mch_name", this.mch_name);
        map.put("openid", this.openid);
        map.put("amount", String.valueOf(this.amount));
        map.put("desc", this.desc);
        map.put("appkey", this.appkey);
        map.put("nonce_str", this.non_str);
        map.put("partner_trade_no", this.partner_trade_no);
        map.put("spbill_create_ip", this.spbill_create_ip);
        return map;
    }

    @Override
    public String toString() {
        return "TransferDto{" +
                "mch_appid='" + mch_appid + '\'' +
                ", mchid='" + mchid + '\'' +
                ", partner_trade_no='" + partner_trade_no + '\'' +
                ", openid='" + openid + '\'' +
                ", check_name='" + check_name + '\'' +
                ", amount=" + amount +
                ", non_str='" + non_str + '\'' +
                ", desc='" + desc + '\'' +
                ", spbill_create_ip='" + spbill_create_ip + '\'' +
                '}';
    }
}
