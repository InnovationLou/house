package xyz.nadev.house.service.impl;

import lombok.extern.slf4j.Slf4j;
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
import java.util.*;

@Slf4j
@Service
public class WxPayServiceImpl implements WxPayService {

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
    @Autowired
    RefundUserRepository refundUserRepository;
    @Autowired
    BillRepository billRepository;
    @Autowired
    HouseSignRepository houseSignRepository;

    /**
     * 用于微信统一下单，获得预支付信息
     *
     * @param money
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public ResponseVO unifiedOrder(Integer houseId, String payItem, BigDecimal money, String token, HttpServletRequest request) throws Exception {
        //终端IP
        String spbill_create_ip = WePayUtil.getIpAddr(request);
        //12位随机字符串
        String out_trade_no = WePayUtil.getNonceStr();
        //看token是否在,若存在，则添加信息到order表
        User user = userServiceImpl.findByToken(token);
        if (user == null) {
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        String openId = user.getOpenId();
        HouseOrder houseOrder = new HouseOrder();
        try {
            try {
                Integer lease = Integer.valueOf(request.getParameter("lease"));
                houseOrder.setLease(lease);
            } catch (Exception e) {
                log.info("不是租房订单");
            }
            houseOrder.setTotalFee(money);
            houseOrder.setOutTradeNo(out_trade_no);
            houseOrder.setOpenId(openId);
            houseOrder.setUserId(user.getId());
            houseOrder.setHouseId(houseId);
            houseOrder.setIsPaid(WxPayConfig.HOUSE_ORDER_NOT_PAID);
            houseOrder.setPayItem(payItem);
            houseorderRepository.save(houseOrder);
            System.out.println("Openid:" + user.getOpenId() + "的order表添加成功，待支付");
        } catch (Exception e) {
            log.error("用户表无此OpenID，添加失败");
            return ControllerUtil.getFalseResultMsgBySelf("用户表无此OpenID，添加失败");
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
            return ControllerUtil.getFalseResultMsgBySelf("mapToXml失败");
        }

        //统一向微信后台下单，返回预支付的订单信息String
        String xmlResult = WePayUtil.httpRequest(payUrl, "POST", xml);
        System.out.println("xmlResult:" + xmlResult);

        try {
            map = WePayUtil.xmlToMap(xmlResult);
            System.out.println("map:" + map);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mapToXml失败");
            return ControllerUtil.getFalseResultMsgBySelf("mapToXml失败");
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
    public String wxNotify(HttpServletRequest request) throws Exception {
        log.info("-------------开始操作HouseOrder--------------");
        //支付成功后完成的逻辑目前是更改houseOrder的支付状态，加入Bill账单记录，加入HouseSign签约记录
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        //sb为微信返回的xml
        String notityXml = sb.toString();
        log.info("notify xml data：" + notityXml);
        String resXml = "";
        Map map = WePayUtil.xmlToMap(notityXml);
        log.info("notify map data:" + map.toString());
        String returnCode = (String) map.get("return_code");
        String out_trade_no = (String) map.get("out_trade_no");
        String transaction_id = (String) map.get("transaction_id");
        //通过订单号查询订单记录
        HouseOrder houseOrder = houseorderRepository.findByOutTradeNo(out_trade_no);
        String payItem = houseOrder.getPayItem();
        if (houseOrder != null) {
            if ("SUCCESS".equals(returnCode)) {
                log.info("用户支付成功");
                try {
                    log.info("我进来了·······");
                    //此账单若是cash房租类，并且有记录数
                    if (houseOrder.getLease() != null && houseOrder.getPayItem() == "cash") {
                        HouseSign houseSign = new HouseSign();
                        houseSign.setId(houseOrder.getUserId());
                        houseSign.setHouseId(houseOrder.getHouseId());
                        houseSign.setGmtCreate(new Date());
                        houseSign.setExpDate(houseOrder.getLease());
                        //结束日期：起止日期+有效期
                        Calendar calendar = Calendar.getInstance();
                        calendar.add(calendar.MONTH, houseOrder.getLease());
                        houseSign.setStartDate(new Date());
                        houseSign.setEndDate(calendar.getTime());
                        houseSign.setIsFulfill(WxPayConfig.HOUSE_IS_FULFILL);
                        houseSign.setIsOut(0);
                        houseSign.setIsDeleted(0);
                        houseSign.setIsPaid(1);
                        houseSign.setOutTradeNo(out_trade_no);
                        houseSignRepository.save(houseSign);
                    }
                    houseOrder.setPrepayId(transaction_id);
                    //将订单支付状态设置为已支付
                    houseOrder.setGmtModify(new Date());
                    houseOrder.setIsPaid(WxPayConfig.HOUSE_ORDER_PAID);
                    houseOrder.setAddrIp(WePayUtil.getIpAddr(request));
                    houseorderRepository.save(houseOrder);
                    //若付款成功，则写入bill表。payiTem来表明是付的什么费
                    Bill bill = new Bill();
                    bill.setUserId(houseOrder.getUserId());
                    bill.setHouseId(houseOrder.getHouseId());
                    bill.setIsPaid(WxPayConfig.HOUSE_ORDER_PAID);
                    bill.setPayItem(payItem);
                    bill.setOutTradeNo(out_trade_no);
                    bill.setMoney(houseOrder.getTotalFee());
                    bill.setPayDate(new Date());
                    billRepository.save(bill);
                    resXml = WePayUtil.NOTIFY_SUCCESS;
                    return resXml;
                } catch (Exception e) {
                    log.error("orderSecretRepository.save(orderSecret)异常发生");
                    resXml = WePayUtil.NOTIFY_FAIL_SERVER_ERROR;
                    log.error(resXml);
                    return resXml;
                }
            }
            resXml = WePayUtil.NOTIFY_FAIL_WRONG_RETURN_CODE;
            return resXml;
        }
        resXml = WePayUtil.NOTIFY_FAIL_UNKNOWN_DATA;
        return resXml;
    }

    /**
     * 微信支付Api打款给用户的方法，商户号到90天直接调用
     *
     * @param
     * @return
     */
    @Override
    public ResultEntity sendMoneyToWechatUser(String openId, BigDecimal amount) {
        if (userServiceImpl.findByOpenId(openId) == null) {
            return null;
        }
        TransferDto transferDto = new TransferDto();
        //生成Partner_trade_no
        String partnerTradeNo = WePayUtil.getNonceStr();
        transferDto.setAppid(appId);  //申请商铺号的APPID
        transferDto.setMch_id(mchId);
        transferDto.setNonce_str(WePayUtil.getNonceStr());
        transferDto.setPartner_trade_no(partnerTradeNo);
        transferDto.setOpenid(openId);
        transferDto.setCheck_name("NO_CHECK");
        //元转换为分
        transferDto.setAmount((amount.multiply(new BigDecimal(100))).intValue());
        transferDto.setAppkey(appSecret);
        transferDto.setSpbill_create_ip(IPUtil.getIPAddressByLast32());
        transferDto.setDesc(desc);
        transferDto.setMch_name("fishHouse");

        String url = transfer;
        String APP_KEY = mchSecret;
        String CERT_PATH = certPath;
        log.info("做成transferDto: " + transferDto.toString());
        try {
            ResultEntity resultEntity = WePayUtil.doPayTransfers(url, APP_KEY, CERT_PATH, transferDto);
            log.info("哈哈,调用成功:" + resultEntity.toString());
            if (resultEntity.isSuccess())
                return resultEntity;
            else
                return null;
        } catch (Exception e) {
            log.error("emmm调用WePayUtil.doTransfers出现了异常:{msg}", e.getMessage());
            return null;
        }
    }

    /**
     * 管理员处理提现请求，失败或拒绝则为user添加回相应的提现金额
     *
     * @param
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseVO dealWithdraw(HttpServletRequest request) {
        try {
            String withdrawMent = request.getParameter("withdrawMent");
            Boolean option = Boolean.valueOf(request.getParameter("option"));

            Withdraw withDraw = withdrawRepository.findByWithdrawMent(withdrawMent);
            if (withDraw == null) {
                return ControllerUtil.getFalseResultMsgBySelf("无此提现订单信息");
            }
            String openid = withDraw.getOpenId();
            // 管理员同意就给他打款，不同意就返回提现金额
            if (option) {
                // 下面条语句用来调用微信后台支付功能
                // ResultEntity rs = sendMoneyToWechatUser(withDraw);
                //log.info("user successfully obtained the money.information:" + rs.toString());
                //String msg = rs.getMsg();
                //找到支付单号,填到数据库
                //withDraw.setWithdrawMent(msg.substring(msg.indexOf("<payment_no><![CDATA[") + "<payment_no><![CDATA[".length(), msg.indexOf("]]></payment_no>")));
                try {
                    TransferRecord transferRecord = new TransferRecord();
                    withDraw.setGmtModify(new Date());
                    withDraw.setIsFinish(WxPayConfig.FINISHED);
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
                        userRepository.save(user);
                        //设置withdraw
                        withDraw.setWithdrawStatus(WxPayConfig.WITHDRAW_ERROR);
                        withDraw.setRemark("管理员拒绝提现，已退回金额");
                        withdrawRepository.save(withDraw);
                        return ControllerUtil.getDataResult("已退还提现金额");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return ControllerUtil.getFalseResultMsgBySelf("提现退款退还异常");
                }
            }
        } catch (Exception e) {
            log.info("获取必要参数参数withdrawMent和option错误");
            return null;
        }

    }

    /**
     * 用户退款
     *
     * @param token
     * @param request
     * @return
     */
    @Override
    public ResponseVO doRefund(String token, HttpServletRequest request) throws Exception {
        //看token是否在,若存在，则添加信息到order表
        User user = userServiceImpl.findByToken(token);
        if (user == null) {
            log.info("失败，获得的token：" + token);
            return ControllerUtil.getFalseResultMsgBySelf("token不存在");
        }
        String openId = user.getOpenId();
        if (WePayUtil.checkSign(request, openId) == false) {
            log.info("sign不正确");
            return ControllerUtil.getFalseResultMsgBySelf("sign不正确");
        }

        try {
            // 提取参数，失败则抛出异常
            String outTradeNo = request.getParameter("outTradeNo");
            RefundUser refundUser = new RefundUser();
            HouseOrder houseOrder = houseorderRepository.findByOpenIdAndOutTradeNo(openId, outTradeNo);
            if (houseOrder == null) {
                log.info("失败,无此订单信息，获得的OpenId：" + token + "  outTradeNo: " + outTradeNo);
                return ControllerUtil.getFalseResultMsgBySelf("无此订单记录");
            }
            BigDecimal refundMoney = houseOrder.getTotalFee();
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
            transferDto.setTotal_fee((refundMoney.multiply(new BigDecimal(100))).intValue());
            transferDto.setRefund_fee((refundMoney.multiply(new BigDecimal(100))).intValue());
            log.info("做成transferDto: " + transferDto.toString());

            refundUser.setOpenId(openId);
            refundUser.setOutTradeNo(outTradeNo);
            refundUser.setGmtCreate(new Date());
            refundUser.setGmtModify(new Date());
            refundUser.setMoney(refundMoney);
            try {
                refundUserRepository.save(refundUser);
            } catch (Exception e) {
                log.error("保存用户退款信息错误");
            }

            try {
                //  将退款请求进行处理
                ResultEntity resultEntity = WePayUtil.doRefundTransfers(refundUrl, mchSecret, certPath, transferDto);
                log.info("哈哈,调用成功:" + resultEntity.toString());
                if (resultEntity.isSuccess()) {
                    refundUser.setIsSuccess(true);
                } else {
                    refundUser.setIsSuccess(false);
                }
                try {
                    refundUserRepository.save(refundUser);
                } catch (Exception e) {
                    log.error("未成功更新退款状态");
                }
                return ControllerUtil.getDataResult(resultEntity);
            } catch (Exception e) {
                log.error("emmm调用WePayUtil.doTransfers出现了异常:{msg}", e.getMessage());
                return ControllerUtil.getFalseResultMsgBySelf("接口调用异常");
            }
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("参数提取失败");
        }
    }


    /**
     * 管理员给用户打款，默认打款到余额，由用户再发起提现请求
     * 应该不得用的方法，
     *
     * @param request
     * @return
     */
    @Override
    public ResponseVO paySomeone(HttpServletRequest request) throws Exception {
        Optional<User> user = userRepository.findById(Integer.valueOf(request.getParameter("userId")));
        if (!user.isPresent()) {
            log.info("查找userId失败");
            return ControllerUtil.getFalseResultMsgBySelf("查找userId失败");
        }
        String openId = user.get().getOpenId();
        if (WePayUtil.checkSign(request, openId) == false) {
            log.info("sign不正确");
            return ControllerUtil.getFalseResultMsgBySelf("sign不正确");
        }
        System.out.println(user.toString());
        User saveUser = userRepository.findByOpenId(user.get().getOpenId());
        try {

            Double money = Double.valueOf(request.getParameter("money"));

            BigDecimal newMoney = user.get().getMoney().add(new BigDecimal(money));
            saveUser.setMoney(newMoney);
            userRepository.save(saveUser);
            return ControllerUtil.getDataResult(user);
        } catch (Exception e) {
            return ControllerUtil.getFalseResultMsgBySelf("保存时发生异常");
        }
    }

}
