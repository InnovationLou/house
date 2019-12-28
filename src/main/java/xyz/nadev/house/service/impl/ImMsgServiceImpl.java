package xyz.nadev.house.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.nadev.house.Enum.ImMsgTypeEnum;
import xyz.nadev.house.comparator.ImMsgComparator;
import xyz.nadev.house.entity.ImMsg;
import xyz.nadev.house.entity.User;
import xyz.nadev.house.repository.ImMsgRepository;
import xyz.nadev.house.repository.UserRepository;
import xyz.nadev.house.service.ImMsgService;
import xyz.nadev.house.service.UserService;
import xyz.nadev.house.util.ControllerUtil;
import xyz.nadev.house.vo.ImChatterHasNewMessage;
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
                .findAllByReadFalseAndReceiverIdAndSenderId(userId,senderId);
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
        if (null == start || null == end){
            start = new Date(new Date().getTime() - 7 * 86400000l);// 默认历史记录7天
            end = new Date();
        }
        int thisId = dataValidate(authorization);
        if (thisId == -1 || null == thatId){
            return ControllerUtil.getFalseResultMsgBySelf(
                    (thisId == -1)?"未查询到接收者信息":"sender is null" + thatId);
        }
        List<ImMsg> imMsgs = imMsgRepository.findAllBySenderIdAndReceiverId(thisId, thatId);
        List<ImMsg> imMsgs1 = imMsgRepository.findAllBySenderIdAndReceiverId(thatId, thisId);
        imMsgs.addAll(imMsgs1);
        // 按照时间规则排序
        imMsgs.sort(new ImMsgComparator());

        Iterator<ImMsg> it = imMsgs.iterator();
        ImMsg tmp;
        while (it.hasNext()){
            tmp = it.next();
            // 移除: 未读 || 早了 || 晚了
            // 2019-12-28 19:15:xx 是否已读不要了
            if (/*!tmp.isRead() ||*/ tmp.getGmtSend().getTime() < start.getTime()
                    || tmp.getGmtSend().getTime() > end.getTime()){
                it.remove();
            }
        }
        return ControllerUtil.getDataResult(imMsgs);
    }

    @Override
    public ResponseVO getChaterListAndLatestWords(String authorization) {
        int userId;
        // 测试时留下的一道后门@ _ @ 2019-12-25 上午
        // 去掉后门 2019-12-25 下午
        userId = /*!authorization.equals("dv5svsvPYA")?*/dataValidate(authorization)/*:9*/;
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
        int friendId;
        User friend;
        List<ImMsg> friendMsgs;
        while(it.hasNext()){
            //每个朋友都要去获取消息和个人信息
            friendId = it.next();
            friend = userRepository.findById(friendId).get();
            imChatterWithLastest100MsgsVO = setWithUser(friend);
            friendMsgs = new ArrayList<>();
            for (ImMsg msg :
                    msgsAsRcvr) {
                if (msg.getReceiverId() == friendId || msg.getSenderId() == friendId){
                    friendMsgs.add(msg);
                }
            }
            friendMsgs.sort(new ImMsgComparator());
            //只要100条信息
            List<ImMsg> imMsgsLimited100 = friendMsgs.subList(0,friendMsgs.size() > 100?100:friendMsgs.size());
            //更改已读属性
            boolean changed = false;
            for (ImMsg msg :
                    imMsgsLimited100) {
                if (!msg.isRead()) {
                    msg.setRead(true);
                    changed = true;
                }
            }
            if (changed){
                imMsgRepository.saveAll(imMsgsLimited100);
            }
            imChatterWithLastest100MsgsVO.setLatest100Msgs(friendMsgs);
            imChatterWithLastest100MsgsVOs.add(imChatterWithLastest100MsgsVO);
        }
        return ControllerUtil.getDataResult(imChatterWithLastest100MsgsVOs);
    }

    @Override
    public ResponseVO getChatterListAndHasNewMessage(String authorization) {
        int userId = /*!authorization.equals("dv5svsvPYA")?*/dataValidate(authorization)/*:9*/;
        if (userId == -1){
            return ControllerUtil.getFalseResultMsgBySelf("错误，没有用户信息");
        }
        // 使用set处理获取朋友IDs
        List<ImMsg> msgsAsRcvr = imMsgRepository.findAllByReceiverId(userId);
        //找朋友
        HashSet<Integer> chatterIds = new HashSet<>();
        for (ImMsg msg:
             msgsAsRcvr) {
            chatterIds.add(msg.getSenderId());
        }
        ImChatterHasNewMessage imChatterHasNewMessage;
        List<ImChatterHasNewMessage> imChatterHasNewMessages = new ArrayList<>();
        Iterator<Integer> it = chatterIds.iterator();
        int friendId;   // 朋友的Id
        int msgCount;   // 未读消息的条数
        User friend;
        while(it.hasNext()){
            friendId = it.next();
            List<ImMsg> friendMsgs = new ArrayList<>();
            for (ImMsg msg:
                    msgsAsRcvr) {
                // 未读
                if (msg.getSenderId() == friendId && !msg.isRead()){
                    friendMsgs.add(msg);
                }
            }
            friend = userRepository.findById(friendId).get();
            imChatterHasNewMessage = setImChatterHasNewMessageWithUser(friend);
            msgCount = friendMsgs.size();

            if (msgCount > 0){
                friendMsgs.sort(new ImMsgComparator());
                imChatterHasNewMessage.setLatestMessage(friendMsgs.get(msgCount - 1));
                imChatterHasNewMessage.setHasNewMessage(true);
                imChatterHasNewMessage.setMessageNumber(msgCount);
            }
            imChatterHasNewMessages.add(imChatterHasNewMessage);
        }
        return ControllerUtil.getDataResult(imChatterHasNewMessages);
    }

    // 不开事务注解会报错, 因为执行了两次删除操作, 需要事务支持
    @Override
    @Transactional
    public ResponseVO deleteChatterAndMsg(String authorization, Integer thatId) {
        if (null == thatId){
            return ControllerUtil.getFalseResultMsgBySelf("thatId为空");
        }
        int userId = dataValidate(authorization);
        if (userId == -1){
            return ControllerUtil.getFalseResultMsgBySelf("该登录用户无效:" + userId);
        }
        imMsgRepository.deleteAllBySenderIdAndReceiverId(userId, thatId);
        imMsgRepository.deleteAllBySenderIdAndReceiverId(thatId, userId);
        return ControllerUtil.getSuccessResultBySelf("删除消息记录成功");

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
        System.out.println(user.toString());
        if (user == null){
            return null;
        }
        ImChatterWithLastest100MsgsVO ic = new ImChatterWithLastest100MsgsVO();
        ic.setAuth(user.getIsAuth() == 1?true:false);
        ic.setCity(user.getCity());
        ic.setId(user.getId());
        ic.setImgUrl(null);
        ic.setLandLord(user.getLandlord() == 1?true:false);
        ic.setNickName(user.getNickName());
        return ic;
    }

    private ImChatterHasNewMessage setImChatterHasNewMessageWithUser(User user){
        ImChatterHasNewMessage ic = new ImChatterHasNewMessage();
        ic.setId(user.getId());
        ic.setAuth(user.getIsAuth() == 1?true:false);
        ic.setCity(user.getCity());
        ic.setImgUrl(null);
        ic.setNickName(user.getNickName());
        ic.setLandLord(user.getLandlord() == 1?true:false);
        return ic;
    }



}
