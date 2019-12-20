package xyz.nadev.house.VO;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信付款上要请求的参数
 */
public class TransferDto {
    //api公用的信息
    private String appid;// 与商铺号关联的应用的appid,这里面指的是小程序的id
    private String mch_id;    // 微信支付分配的商铺号
    private String openid; //用户的openid 众所周知 每个用户使用的每个小程序的openid不一样
    private String appkey; //注：key为商户平台设置的密钥key
    private String nonce_str;    //随机字符串, 不长于32位
    private String spbill_create_ip = "45.40.193.214";//ip信息

    //统一下单附加上的要用的参数
    private String out_trade_no;  //商户订单号 自己生成的 需要保持唯一(支付失败时由该订单号保持状态，若此号改变，则可能产生重复支付的情况)
    private int total_fee;   //订单总金额金额数 单位是分
    private String notify_url;//异步接收微信支付结果通知的回调地址
    private String trade_type;//小程序取值如下：JSAPI
    private String body;//商品简单描述，该字段请按照规范传


    //退款接口要附加的参数
    private  int refund_fee;    //退款金额
    private String transaction_id;    //微信后台生成的订单号，在支付通知中有返回
    private String out_refund_no; //商户系统内部的退款单号，商户系统内部唯一





    //企业付款给用户要用的
    private String partner_trade_no;//商户订单号
    private String check_name = "NO_CHECK";//校验真实姓名 NO_CHECK不校验   FORCE_CHECK强制校验
    private int amount;//企业付款金额，单位为分
    // 商户名称, 如'XXX服务号'
    private String mch_name;
    private String desc;//企业付款备注





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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public int getRefund_fee() {
        return refund_fee;
    }

    public void setRefund_fee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
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


    public int getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(int total_fee) {
        this.total_fee = total_fee;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOut_refund_no() {
        return out_refund_no;
    }

    public void setOut_refund_no(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    //用户退款
    @Transient
    public Map<String, String> map()
    {
        Map<String, String> map = new HashMap<String, String>();
        map.put("mch_appid", this.appid);
        map.put("mchid", this.mch_id);
        map.put("mch_name", this.mch_name);
        map.put("openid", this.openid);
        map.put("total_fee", String.valueOf(this.total_fee));
        map.put("nonce_str", this.nonce_str);
        map.put("out_trade_no", this.out_trade_no);
        return map;
    }



}
