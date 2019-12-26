package xyz.nadev.house.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.nadev.house.Enum.ImMsgTypeEnum;
import xyz.nadev.house.comparator.ImMsgComparator;
import xyz.nadev.house.entity.ImMsg;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.repository.ImMsgRepository;
import xyz.nadev.house.repository.UserRepository;
import xyz.nadev.house.service.ImMsgService;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ImChatterWithLastest100MsgsVO;
import xyz.nadev.house.vo.ResponseVO;

import java.util.*;

@Service
@Slf4j
public class ImMsgServiceImpl implements ImMsgService {

    @Autowired
    UserService userService;
    @Autowired
    ImMsgRepository imMsgRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public ResponseVO sendMsg(String authorization, String type, String msg, Integer receiverId, Integer houseId) {
        boolean valid = dataValidate(authorization, type, msg, receiverId, houseId);
        if (!valid){
            return ControllerUtil.getFalseResultMsgBySelf("数据校验错误, 发送消息失败");
        }
        User sender = userService.findByToken(authorization);
        ImMsg imMsg = new ImMsg();
        imMsg.setHouseId(houseId);
        imMsg.setMsg(msg);
        imMsg.setRead(false);
        imMsg.setType(type);
        imMsg.setSenderId(sender.getId());
        imMsg.setReceiverId(receiverId);
        imMsg.setGmtSend(new Date());
        imMsg.setGmtRead(new Date());
        return ControllerUtil.getDataResult(imMsgRepository.save(imMsg));
    }

    @Override
    public ResponseVO receiveFreshMsg(String authorization, Integer senderId) {
        //sender id 未校验
        int userId = dataValidate(authorization);
        if (userId == -1){
            return ControllerUtil.getFalseResultMsgBySelf("用户信息校验错误, 无法获取用户的消息列表");
        }
        List<ImMsg> msgs = imMsgRepository
                .findAllByReadFalseAndReceiverIdEqualsAnAndSenderIdEquals(userId,senderId);
        for (ImMsg msg :
                msgs) {
            msg.setRead(true);
            msg.setGmtRead(new Date());
        }
        // 将其变成已读状态
        imMsgRepository.saveAll(msgs);
        for (ImMsg msg :
                msgs) {
            msg.setRead(false);
            msg.setGmtRead(null);
        }
        return ControllerUtil.getDataResult(msgs);
    }

    @Override
    public ResponseVO searchHistoryMsg(String authorization, Date start, Date end, Integer thatId) {
        int thisId = dataValidate(authorization);
        if (thisId == -1 || null == thatId){
            return ControllerUtil.getFalseResultMsgBySelf(
                    (thisId == -1)?"未查询到接收者信息":"sender is null" + thatId);
        }
        List<ImMsg> imMsgs = imMsgRepository.findAllByReceiverIdAndSenderId(thisId, thatId);
        List<ImMsg> imMsgs1 = imMsgRepository.findAllBySenderIdAndReceiverId(thatId, thisId);
        imMsgs.addAll(imMsgs1);
        // 按照时间规则排序
        imMsgs.sort(new ImMsgComparator());

        Iterator<ImMsg> it = imMsgs.iterator();
        ImMsg tmp;
        while (it.hasNext()){
            tmp = it.next();
            // 移除: 未读 || 早了 || 晚了
            if (!tmp.isRead() || tmp.getGmtSend().getTime() < start.getTime()
                    || tmp.getGmtSend().getTime() > end.getTime()){
                it.remove();
            }
        }
        return ControllerUtil.getDataResult(imMsgs);
    }

    @Override
    public ResponseVO getChaterListAndLatestWords(String authorization) {
        int userId = dataValidate(authorization);
        if (userId == -1){
            return ControllerUtil.getFalseResultMsgBySelf("错误，没有用户信息");
        }
        // 使用set处理获取朋友IDs
        List<ImMsg> msgsAsRcvr = imMsgRepository.findAllByReceiverId(userId);
        List<ImMsg> msgsAsSender = imMsgRepository.findAllBySenderId(userId);
        // 作为接收消息的人, 有哪些人给我发过消息
        msgsAsRcvr.addAll(msgsAsSender);
        HashSet<Integer> friendIds = new HashSet<>();

        // 这里的msgs是合并后的msgs
        for (ImMsg msg :
                msgsAsRcvr) {
            if (msg.getReceiverId() == userId){
                friendIds.add(msg.getSenderId());
            }else {
                friendIds.add(msg.getReceiverId());
            }
        }

        List<ImChatterWithLastest100MsgsVO> imChatterWithLastest100MsgsVOs =
                new ArrayList<>();
        ImChatterWithLastest100MsgsVO imChatterWithLastest100MsgsVO;
        Iterator<Integer> it = friendIds.iterator();
        int friendId = -1;
        User user;
        while(it.hasNext()){
            //每个朋友都要去获取消息和个人信息
            friendId = it.next();
            imChatterWithLastest100MsgsVO = new ImChatterWithLastest100MsgsVO();
            user = userRepository.findById(userId);

        }


        return null;
    }


    //-------------私有方法--------------//
    // 发送消息数据校验方法
    private boolean dataValidate(String authorization, String type, String msg, Integer receiverId, Integer houseId){
        try {
            User sender = userService.findByToken(authorization);
            if (null == sender){
                log.error("发送者信息有误,没有查询到该这个发送者");
                return false;
            }
            // 数据校验
            User receiver = userService.findById(receiverId);
            if (null == receiver){
                log.error("消息发送出错: 查无此接受者");
                return false;
            }
            if (type.equals(ImMsgTypeEnum.PRODUCT) && houseId == null){
                log.error("没有传入房屋信息的ID");
                return false;
            }
            int max_len = 255;//消息发送的最大程度
            if (null != msg && msg.length() > max_len){
                log.error("消息发送长度过长，出现错误");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 校验用户信息,存在返回userId,不存在返回-1
    private int dataValidate(String authorization){
        try {
            User user = userService.findByToken(authorization);
            if (null == user){
                log.error("没有找到用户, 无法查询该用户的新消息");
                return -1;
            }
            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private ImChatterWithLastest100MsgsVO setWithUser(User user){
        if (user == null){
            return null;
        }
        ImChatterWithLastest100MsgsVO ic = new ImChatterWithLastest100MsgsVO();

    }

}
