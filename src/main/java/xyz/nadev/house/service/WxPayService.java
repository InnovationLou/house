package xyz.nadev.house.service;

import xyz.nadev.house.entity.Withdraw;
import xyz.nadev.house.VO.ResponseVO;
import xyz.nadev.house.VO.ResultEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface WxPayService {
    /**
     * 预支付请求
     * @param outTradeNo
     * @param money
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    ResponseVO unifiedOrder(String outTradeNo, BigDecimal money, String token, HttpServletRequest request) throws Exception;

    /**
     * 微信回调处理接口
     * @param request
     * @return
     * @throws Exception
     */
    ResponseVO wxNotify(HttpServletRequest request) throws Exception;

    /**
     * 付钱给提现那边
     * @param withdraw
     * @return
     */
    ResultEntity sendMoneyToWechatUser(Withdraw withdraw);

    /**
     * 处理提现请求接口
     * @param withdrawMent
     * @return
     */
    ResponseVO dealWithdraw(String withdrawMent, Boolean option);

    /**
     * 用户退款
     * @param outTradeNo
     * @param token
     * @return
     */
    ResponseVO doRefund(String outTradeNo, String token);


}
