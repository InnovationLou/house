package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.vo.ResultEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface WxPayService {
    /**
     * 预支付请求
     * @param money
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    ResponseVO unifiedOrder(BigDecimal money, String token, HttpServletRequest request) throws Exception;

    /**
     * 微信回调处理接口
     * @param request
     * @return
     * @throws Exception
     */
    ResponseVO wxNotify(HttpServletRequest request) throws Exception;

    /**
     * 付钱给提现那边
     * @param openId
     * @param amount
     * @return
     */
    ResultEntity sendMoneyToWechatUser(String openId, BigDecimal amount);

    /**
     * 处理提现请求接口
     * @param withdrawMent
     * @return
     */
    ResponseVO dealWithdraw(String withdrawMent, Boolean option);

    /**
     * 用户退款
     * @param token
     * @param request
     * @return
     */
    ResponseVO doRefund(String token,HttpServletRequest request) throws Exception;

    ResponseVO paySomeone(HttpServletRequest request) throws Exception;

}
