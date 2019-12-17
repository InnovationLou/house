package xyz.nadev.house.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.config.WxPayConfig;
import xyz.nadev.house.entity.HouseOrder;
import xyz.nadev.house.repository.HouseOrderRepository;
import xyz.nadev.house.repository.UserRepository;
import xyz.nadev.house.service.WxPayService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.WePayUtil;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {
    Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    @Autowired
    private HouseOrderRepository houseorderRepository;

//    @Value("${wx.app-id}")
//    private String appId;

//    @Value("${wx.app-secret}")
//    private String appSecret;
//
//    @Value("${wx.mch-id}")
//    private String mchId;
//    @Value("${wx.mch-secret}")
//    private String mchSecret;
//    @Value("${wx.notify-url}")
//    private String notifyUrl;
//    @Value("${wx.pay-url}")
//    private String payUrl;
//    @Value("${wx.trade-type}")
//    private String tradeType;
//    @Value("${wx.body}")
//    private String body;


    @Autowired
    private UserRepository userRepository;

    public ResponseVO unifiedOrder(String out_trade_no, BigDecimal money, String openId, HttpServletRequest request) throws Exception {
//        //查看此OpenID是否在
//        try {
//            User user = userRepository.findByOpenId(openId);
//            System.out.println("Openid:"+user.getOpenId());
//        }catch (Exception e){
//            logger.error("用户表无此OpenID");
//            return null;
//        }

        Map<String, String> reqParams = new HashMap<>();
        //装填request 参数    开始。。。。。。

        String nonceStr = WePayUtil.getNonceStr();
        System.out.println("生成的随机字符串为：" + nonceStr);
        //微信分配的小程序ID
        reqParams.put("appid", "wx13d9cca9b74bc90c");
        //微信支付分配的商户号
        reqParams.put("mch_id", "1540438471");
        //随机字符串
        reqParams.put("nonce_str", nonceStr);
        //充值订单 商品描述
        reqParams.put("body", "ssssssssssssss");
        //商户订单号
        reqParams.put("out_trade_no", out_trade_no + "");
        //订单总金额，单位为分
        reqParams.put("total_fee", money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        System.out.println("金额为：" + money.multiply(BigDecimal.valueOf(100)).intValue() + "");
        //终端IP
        String spbill_create_ip = WePayUtil.getIpAddr(request);
        System.out.println("用户ip" + spbill_create_ip);
        reqParams.put("spbill_create_ip", spbill_create_ip);
        //通知地址
        reqParams.put("notify_url", "127.0.0.1:8080/pay/wxNotify");
        //交易类型
        reqParams.put("trade_type","JSAPI");
        //用户标识
        reqParams.put("openid", openId + "");
        //排序签名
        String prestr = WePayUtil.createLinkString(reqParams);
        System.out.println("排序后的签名字段：" + prestr);
        String mysign = WePayUtil.sign(prestr, "JQM66688JQM66688JQM66688JQM66688", "utf-8").toUpperCase();
        System.out.println("生成签名" + mysign);
        reqParams.put("sign", mysign);
        System.out.println("生成预支付信息 - 请求prepa带y所携参数:" + reqParams.toString());
        /*
            调用支付定义下单API,返回预付单信息 prepayId
            //先转换为xmp，在发起请求
         */
        String xml = null;
        Map map = null;
        try {
            xml = WePayUtil.mapToXml(reqParams);
            System.out.println("最终提交的xml: " + xml);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mapToXml失败");
            return null;
        }

        //统一向微信后台下单，返回预支付的订单信息String
        String xmlResult = WePayUtil.httpRequest(WxPayConfig.PAY_URL, "POST", xml);
        System.out.println("xmlResult:" + xmlResult);

        try {
            map = WePayUtil.xmlToMap(xmlResult);
            System.out.println("map:" + map);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("xmlToMap失败");
            return null;
        }
        String return_code = (String) map.get("return_code");//返回状态码
        String result_code = (String) map.get("result_code");//返回状态码
        Map<String, String> response = new HashMap<>();//返回给小程序端需要的参数
        if (return_code.equals("SUCCESS") && return_code.equals(result_code)) {
            System.out.println("得到成功的服务器返回：resultcode" + result_code + ",returncode:" + return_code);
            String prepay_id = (String) map.get("prepay_id");//返回的预付单信息
            response.put("nonceStr", nonceStr);
            response.put("package", "prepay_id=" + prepay_id);
            response.put("appid", WxPayConfig.APP_ID);
            Long timeStamp = System.currentTimeMillis() / 1000;
            response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //拼接签名需要的参数
            String stringSignTemp = "appId=" + WxPayConfig.APP_ID + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = WePayUtil.sign(stringSignTemp, WxPayConfig.MCH_SECRET, "utf-8").toUpperCase();
            response.put("paySign", paySign);
        }
        if (response.get("paySign") == null) {
            response.put("error", "索取预支付信息失败！");
            response.put("wx server msg", map.toString());
        }
        System.out.println("获取预支付-response is:" + response.toString());
        return ControllerUtil.getSuccessResultBySelf(response);
    }

    @Override
    public ResponseVO wxNotify(HttpServletRequest request) throws Exception {
        logger.info("-------------开始操作HouseOrder--------------");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        //sb为微信返回的xml
        String notityXml = sb.toString();
        logger.info("notify xml data：" + notityXml);
        String resXml = "";
        Map map = WePayUtil.xmlToMap(notityXml);
        logger.info("notify map data:" + map.toString());
        String returnCode = (String) map.get("return_code");
        String out_trade_no = (String) map.get("out_trade_no");
        String transaction_id = (String) map.get("transaction_id");
        if ("SUCESS".equals(returnCode)) {
            HouseOrder houseOrder = houseorderRepository.findByOutTradeNoAndPrepayId(out_trade_no, transaction_id);
            houseOrder.setIsPaid(1);
            if (houseOrder != null) {
                try {
                    houseorderRepository.save(houseOrder);
                    resXml = WePayUtil.NOTIFY_SUCCESS;
                } catch (Exception e) {
                    logger.error("orderSecretRepository.save(orderSecret)异常发生");
                    resXml = WePayUtil.NOTIFY_FAIL_SERVER_ERROR;
                    logger.error(resXml);
                    return ControllerUtil.getFalseResultMsgBySelf(resXml);
                }
            }
            return ControllerUtil.getFalseResultMsgBySelf("没有此订单信息");

        }
        return null;
    }
}
