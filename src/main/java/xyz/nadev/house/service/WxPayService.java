package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public interface WxPayService {
    ResponseVO unifiedOrder(String out_trade_no, BigDecimal money, String openId, HttpServletRequest request) throws Exception;
    ResponseVO wxNotify(HttpServletRequest request) throws Exception;
}
