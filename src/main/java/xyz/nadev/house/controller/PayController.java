package xyz.nadev.house.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nadev.house.service.WxPayService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/pay")
public class PayController {

    Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private WxPayService wxPayService;

    /** 创建订单，并获取预支付信息
     * openId 已获取到openID，只需要创建订单就行
     * @param openId 用户openId
     * @param money 金额
     * @return
     */
    @PostMapping("/getPrepayInfo")
    public ResponseVO order(String orderId, String openId, BigDecimal money, HttpServletRequest request) throws Exception {

        //12位随机字符串
//        String out_trade_no = WePayUtil.getNonceStr();
        String out_trade_no = "345671892";

        System.out.println("---------------程序进来了--------------------");
        System.out.println(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));

        Object object = wxPayService.unifiedOrder(out_trade_no,money,openId,request);
        return ControllerUtil.getSuccessResultBySelf(object);
    }

    @PostMapping("/wxNotify")
    public ResponseVO notify(HttpServletRequest request){
        //在订单发起后，应该有写入订单表操作
        return null;
    }

    @PostMapping("/pay/someone")
    private ResponseVO paySomeone(){
        return null;
    }




}
