package xyz.nadev.house.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xyz.nadev.house.service.WxPayService;
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
    @GetMapping("/prepayInfo/{houseId}/{payItem}/{money}")
    public ResponseVO order(@RequestHeader("Authorization") String token,
                            @PathVariable Integer houseId,
                            @PathVariable String payItem,
                            @PathVariable BigDecimal money,
                            HttpServletRequest request) throws Exception {
        return wxPayService.unifiedOrder(houseId,payItem,money,token, request);
    }

    @ApiOperation(value = "微信回调接口")
    @PostMapping("/wxNotify")
    public ResponseVO notify(HttpServletRequest request) throws Exception {
        return wxPayService.wxNotify(request);
    }

    @ApiOperation(value = "管理员处理提现请求")
    @PostMapping("/withdraw/{withdrawMent}")
    private ResponseVO payWithdraw(@PathVariable String withdrawMent, Boolean option,HttpServletRequest request) {
        return wxPayService.dealWithdraw(request);
    }

    @ApiOperation(value = "如果有需要，管理员可以给用户余额添加金额")
    @PostMapping("/someone")
    public ResponseVO paySomeone(String userId, BigDecimal money ,String sign,HttpServletRequest request) throws Exception {
        return wxPayService.paySomeone(request);
    }

}
