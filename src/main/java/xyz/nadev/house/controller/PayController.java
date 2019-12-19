package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.nadev.house.service.WxPayService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.util.WePayUtil;
import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/pay")
public class PayController {

    Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private WxPayService wxPayService;

    @ApiOperation(value = "获取预支付信息")
    @PostMapping("/prepayInfo")
    public ResponseVO order(@RequestHeader("Authorization") String token, BigDecimal money, HttpServletRequest request) throws Exception {

        //12位随机字符串
        String out_trade_no = WePayUtil.getNonceStr();

        System.out.println("---------------程序进来了--------------------");
//        System.out.println(UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32));

        Object object = wxPayService.unifiedOrder(out_trade_no, money, token, request);
        return ControllerUtil.getSuccessResultBySelf(object);
    }

    @ApiOperation(value = "微信回调接口")
    @PostMapping("/wxNotify")
    public ResponseVO notify(HttpServletRequest request) {
        try {
            return ControllerUtil.getDataResult(wxPayService.wxNotify(request));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @ApiOperation(value = "管理员处理提现请求")
    @PostMapping("/withdraw/{withdrawMent}")
    private ResponseVO payWithdraw(String withdrawMent, Boolean option) {
        return ControllerUtil.getDataResult(wxPayService.dealWithdraw(withdrawMent,option));
    }
}
