package xyz.nadev.house.service;

import xyz.nadev.house.vo.ResponseVO;

import java.util.Date;

/**
 *
 * 聊天服务
 * 提供一系列聊天需要的方法
 *
 */
public interface ImMsgService {
    /**
     * 发送一条信息给某某人
     * @param authorization 头信息，获取发送者的信息
     * @param type 消息类型, 默认为message, 即普通消息, 前台可以指定 product, 此时必须传入 houseId
     * @param msg 消息内容,纯文本
     * @param receiverId 消息发送给谁, 不可为空
     * @param houseId type为'product'时才传入,否则不传入
     * @return 消息发送成功与否
     */
    ResponseVO sendMsg(String authorization, String type, String msg, Integer receiverId, Integer houseId);

    /**
     * 获取该用户的未读取的消息
     * @param authorization
     * @return 未读取消息的列表
     */
    ResponseVO receiveFreshMsg(String authorization, Integer senderId);

    /**
     * 根据传入的时间段信息查询历史信息
     * @param authorization
     * @param start 开始日期
     * @param end 截止日期
     * @return
     */
    ResponseVO searchHistoryMsg(String authorization, Date start, Date end, Integer senderId);

    /**
     * 根据传入的auth信息找到对应用户的朋友和对话信息
     * @param authorization
     * @return 包含朋友的基础信息和最近的一句聊天话
     */
    ResponseVO getChaterListAndLatestWords(String authorization);

}
