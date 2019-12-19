package xyz.nadev.house.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nadev.house.config.WxPayConfig;
import xyz.nadev.house.entity.*;
import xyz.nadev.house.repository.*;
import xyz.nadev.house.service.WxPayService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.IPUtil;
import xyz.nadev.house.util.WePayUtil;
import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.vo.ResultEntity;
import xyz.nadev.house.vo.TransferDto;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPayServiceImpl implements WxPayService {
    Logger logger = LoggerFactory.getLogger(WxPayServiceImpl.class);

    @Value("${wx.app.id}")
    private String appId;
    @Value("${wx.app.secret}")
    private String appSecret;
    @Value("${wx.mch.id}")
    private String mchId;
    @Value("${wx.mch.secret}")
    private String mchSecret;
    @Value("${wx.url.notify}")
    private String notifyUrl;
    @Value("${wx.url.pay}")
    private String payUrl;
    @Value("${wx.trade.type}")
    private String tradeType;
    @Value("${wx.body}")
    private String body;
    @Value("${wx.desc}")
    private String desc;
    @Value("${wx.url.transfer}")
    private String transfer;
    @Value("${wx.certPath}")
    private String certPath;
    @Value("${wx.url.refund}")
    private String refundUrl;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    WithdrawRepository withdrawRepository;
    @Autowired
    HouseOrderRepository houseorderRepository;
    @Autowired
    HouseRepository houseRepository;
    @Autowired
    TransferRecordRepository transferRecordRepository;

    /**
     * 用于微信统一下单，获得预支付信息
     *
     * @param out_trade_no
     * @param money
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public ResponseVO unifiedOrder(String out_trade_no, BigDecimal money, String token, HttpServletRequest request) throws Exception {
        //看token是否在,若存在，则添加信息到order表
        User user = userServiceImpl.findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        String openId = user.getOpenId();
        HouseOrder houseOrder = new HouseOrder();
        try {
            houseOrder.setTotalFee(money);
            houseOrder.setOutTradeNo(out_trade_no);
            houseOrder.setOpenId(openId);
            houseOrder.setIsPaid(WxPayConfig.HOUSE_ORDER_NOT_PAID);
            houseorderRepository.save(houseOrder);
            System.out.println("Openid:" + user.getOpenId() + "order表添加成功");
        } catch (Exception e) {
            logger.error("用户表无此OpenID，添加失败");
            return null;
        }

        Map<String, String> reqParams = new HashMap<>();
        //装填request 参数    开始。。。。。。

        String nonceStr = WePayUtil.getNonceStr();
        System.out.println("生成的随机字符串为：" + nonceStr);
        //微信分配的小程序ID
        reqParams.put("appid", appId);
        //微信支付分配的商户号
        reqParams.put("mch_id", mchId);
        //随机字符串
        reqParams.put("nonce_str", nonceStr);
        //充值订单 商品描述
        reqParams.put("body", body);
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
        reqParams.put("notify_url", notifyUrl);
        //交易类型
        reqParams.put("trade_type", tradeType);
        //用户标识
        reqParams.put("openid", openId + "");
        //排序签名
        String prestr = WePayUtil.createLinkString(reqParams);
        System.out.println("排序后的签名字段：" + prestr);
        String mysign = WePayUtil.sign(prestr, mchSecret, "utf-8").toUpperCase();
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
        String xmlResult = WePayUtil.httpRequest(payUrl, "POST", xml);
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
            response.put("appid", appId);
            Long timeStamp = System.currentTimeMillis() / 1000;
            response.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //拼接签名需要的参数
            String stringSignTemp = "appId=" + appId + "&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + timeStamp;
            //再次签名，这个签名用于小程序端调用wx.requesetPayment方法
            String paySign = WePayUtil.sign(stringSignTemp, mchSecret, "utf-8").toUpperCase();
            response.put("paySign", paySign);
        }
        if (response.get("paySign") == null) {
            response.put("error", "索取预支付信息失败！");
            response.put("wx server msg", map.toString());
        }
        System.out.println("获取预支付-response is:" + response.toString());
        return ControllerUtil.getSuccessResultBySelf(response);
    }

    /**
     * 微信回调函数
     *
     * @param request
     * @return
     * @throws Exception
     */
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
            //通过订单号查询订单记录
            HouseOrder houseOrder = houseorderRepository.findByOutTradeNo(out_trade_no);
            if (houseOrder != null) {
                try {
                    houseOrder.setPrepayId(transaction_id);
                    //将订单支付状态设置为翼支付
                    houseOrder.setIsPaid(WxPayConfig.HOUSE_ORDER_PAID);
                    houseorderRepository.save(houseOrder);
                    resXml = WePayUtil.NOTIFY_SUCCESS;
                    return ControllerUtil.getDataResult(resXml);
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

    /**
     * 微信支付Api打款给用户的方法，直接调用  待修改
     *
     * @param
     * @return
     */
    @Override
    public ResultEntity sendMoneyToWechatUser(Withdraw withdraw) {
        House house = new House();
        try {
            house = houseRepository.findByUserId(withdraw.getOpenId());
            System.out.println("拿到USER：" + house.toString());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("查询user时出错,USER ID:" + withdraw.getOpenId());
        }
        String openId = "";

        TransferDto transferDto = new TransferDto();

        //元转换为分
        transferDto.setAmount((withdraw.getMoney().multiply(new BigDecimal(100))).intValue());
        transferDto.setAppkey(appSecret);
        transferDto.setSpbill_create_ip(IPUtil.getIPAddressByLast32());
        transferDto.setCheck_name("NO_CHECK");
        transferDto.setDesc(desc);
        transferDto.setAppid(appId);  //申请商铺号的APPID
        transferDto.setMch_name("fishHouse");
        transferDto.setMch_id(mchId);
        transferDto.setNonce_str(WePayUtil.getNonceStr());
        transferDto.setOpenid(openId);
        transferDto.setPartner_trade_no(withdraw.getWithdrawMent());

        String url = transfer;
        String APP_KEY = appSecret;
        String CERT_PATH = certPath;
        logger.info("做成transferDto: " + transferDto.toString());
        try {
            ResultEntity resultEntity = WePayUtil.doPayTransfers(url, APP_KEY, CERT_PATH, transferDto);
            logger.info("哈哈,调用成功:" + resultEntity.toString());
            if (resultEntity.isSuccess())
                return resultEntity;
            else
                return null;
        } catch (Exception e) {
            logger.error("emmm调用WePayUtil.doTransfers出现了异常:{msg}", e.getMessage());
            return null;
        }
    }

    /**
     * 为user添加回相应的提现金额
     *
     * @param
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO dealWithdraw(String withdrawMent, Boolean option) {
        Withdraw withDraw = withdrawRepository.findByWithdrawMent(withdrawMent);
        if (withDraw == null) {
            return ControllerUtil.getFalseResultMsgBySelf("无此提现订单信息");
        }
        String openid = withDraw.getOpenId();
        if (option) {
            // 下面条语句用来调用微信后台支付功能
            // ResultEntity rs = sendMoneyToWechatUser(withDraw);
            //logger.info("user successfully obtained the money.information:" + rs.toString());
            //String msg = rs.getMsg();
            //找到支付单号,填到数据库
            //withDraw.setWithdrawMent(msg.substring(msg.indexOf("<payment_no><![CDATA[") + "<payment_no><![CDATA[".length(), msg.indexOf("]]></payment_no>")));
            try {
                TransferRecord transferRecord = new TransferRecord();
                withDraw.setGmtModify(new Date());
                withDraw.setIsFinish(1);
                transferRecord.setWithdrawMent(withdrawMent);
                transferRecord.setWxId(withDraw.getWxId());
                transferRecord.setGmtCreate(new Date());
                transferRecord.setGmtCreate(new Date());
                transferRecord.setTransferMoney(withDraw.getMoney());
                transferRecordRepository.save(transferRecord);
                withdrawRepository.save(withDraw);
                return ControllerUtil.getDataResult(withDraw);
            } catch (Exception e) {
                e.printStackTrace();
                return ControllerUtil.getFalseResultMsgBySelf("保存提现信息异常");
            }
        } else {
            //拒绝则归还原先提现请求时扣除的佣金金额;
            try {
                User user = userRepository.findByOpenId(openid);
                //将提现金额退还到余额
                BigDecimal retMoney = user.getMoney().add(withDraw.getMoney());

                if (user == null) {
                    return null;
                } else {
                    //设置用户余额
                    user.setMoney(retMoney);
                    //设置withdraw
                    withDraw.setWithdrawStatus(0);
                    userRepository.save(user);
                    withdrawRepository.save(withDraw);
                    return ControllerUtil.getDataResult("已退还提现金额");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ControllerUtil.getFalseResultMsgBySelf("提现退款退还异常");
            }
        }
    }

    /**
     * 用户退款
     *
     * @param outTradeNo
     * @param token
     * @return
     */
    @Override
    public ResponseVO doRefund(String outTradeNo, String token) {
        //看token是否在,若存在，则添加信息到order表
        User user = userServiceImpl.findByToken(token);
        if (user == null) {
            logger.info("失败，获得的token：" + token);
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        String openId = user.getOpenId();

        HouseOrder houseOrder = houseorderRepository.findByOpenIdAndOutTradeNo(openId, outTradeNo);
        if (houseOrder == null) {
            logger.info("失败，获得的OpenId：" + token + "  outTradeNo: " + outTradeNo);
            return ControllerUtil.getFalseResultMsgBySelf("无此订单记录");
        }
        BigDecimal RefundMoney = houseOrder.getTotalFee();
        String nonceStr = WePayUtil.getNonceStr();
        //装填请求参数，8个，详情见https://api.mch.weixin.qq.com/secapi/pay/refund
        TransferDto transferDto = new TransferDto();
        transferDto.setAppid(appId);
        transferDto.setMch_id(mchId);
        transferDto.setNonce_str(nonceStr);
        //	transaction_id 和out_trade_no二选一
        transferDto.setOut_trade_no(outTradeNo);
        transferDto.setOut_refund_no(WePayUtil.getNonceStr());
        //元转换为分
        transferDto.setTotal_fee((RefundMoney.multiply(new BigDecimal(100))).intValue());
        transferDto.setRefund_fee((RefundMoney.multiply(new BigDecimal(100))).intValue());
        logger.info("做成transferDto: " + transferDto.toString());
        try {
            //  将退款请求进行处理
            ResultEntity resultEntity = WePayUtil.doRefundTransfers(refundUrl, mchSecret, certPath, transferDto);
            logger.info("哈哈,调用成功:" + resultEntity.toString());
            if (resultEntity.isSuccess())
                return ControllerUtil.getDataResult(resultEntity);
            else
                return ControllerUtil.getDataResult(resultEntity);
        } catch (Exception e) {
            logger.error("emmm调用WePayUtil.doTransfers出现了异常:{msg}", e.getMessage());
            return null;
        }
    }

}
