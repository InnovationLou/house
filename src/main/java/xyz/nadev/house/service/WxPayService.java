package xyz.nadev.house.service;

import xyz.nadev.house.entity.Withdraw;
import xyz.nadev.house.vo.ResponseVO;
import xyz.nadev.house.vo.ResultEntity;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface WxPayService {
    ResponseVO unifiedOrder(String out_trade_no, BigDecimal money, String openId, HttpServletRequest request) throws Exception;
    ResponseVO wxNotify(HttpServletRequest request) throws Exception;
    ResultEntity sendMoneyToWechatUser(Withdraw withdraw);
    ResponseVO updateWithDraw(String openId, Boolean option, String withdrawMent);
}
